package com.codacy.client.bitbucket.client

import java.net.URI

import BodyWriteableWrapper.BodyWriteable
import DefaultBodyWritables._
import WSWrapper.WSClient
import com.codacy.client.bitbucket.client.Authentication._
import com.codacy.client.bitbucket.util.HTTPStatusCodes
import com.codacy.client.bitbucket.util.Implicits.URIQueryParam
import play.api.libs.json._
import com.codacy.client.bitbucket.util.UrlHelper._

import scala.compat.Platform.EOL
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}
import scala.collection.parallel.immutable.ParRange
import scala.util.{Failure, Properties, Success, Try}

object BitbucketClientBase {
  val apiBaseUrl = "https://bitbucket.org/api/2.0"
}

abstract class BitbucketClientBase(val client: WSClient, credentials: Credentials) {

  val apiBaseUrl = BitbucketClientBase.apiBaseUrl
  val userBaseUrl = s"$apiBaseUrl/user"
  val usersBaseUrl = s"$apiBaseUrl/users"
  val repositoriesBaseUrl = s"$apiBaseUrl/repositories"
  val workspacesBaseUrl = s"$apiBaseUrl/workspaces"

  private lazy val requestTimeout = Duration(20, SECONDS)

  private lazy val authenticator = Authenticator.fromCredentials(credentials)

  /**
    * Does an API request and parses the json output into a class.
    */
  def execute[T: Reads](url: String): RequestResponse[T] =
    get(url) match {
      case Right(json) =>
        json.validate[T].fold(e => FailedResponse(s"Failed to parse json ($e): $json"), SuccessfulResponse(_))
      case Left(error) => FailedResponse(error.detail)
    }

  /**
    * Make an API request and parse the json output into a response. This response includes not only the objects
    * requested but pagination information as well.
    *
    * @param url The url to be invoked if no cursor is provided
    * @param pageRequest The pagination request with cursor information
    * @param reader A [[Reads]] to parse the json response from the API
    * @tparam T The type of the objects in the response (excluding pagination information)
    * @return A [[SuccessfulResponse]] or a [[FailedResponse]] depending if the request was successful or not
    */
  def executeWithCursor[T](url: String, pageRequest: PageRequest, pageLength: Option[Int] = None)(
      implicit reader: Reads[T]
  ): RequestResponse[Seq[T]] = {
    val pageLengthParameter = pageLength.fold("")(pagelen => s"pagelen=$pagelen")
    val requestUrl = pageRequest.cursor match {
      // Here we can just return the cursor because in BitBucket the cursor is a well-formed URL that must
      // be used directly (according to the API documentation)
      case Some(cursor) => cursor
      case None => joinQueryParameters(url, pageLengthParameter)
    }

    get(requestUrl) match {
      case Right(json) =>
        (json \ "values")
          .validate[Seq[T]]
          .fold(
            e => FailedResponse(s"Failed to parse json ($e): $json"),
            values =>
              SuccessfulResponse(
                values,
                size = (json \ "size").asOpt[Int],
                pageLen = (json \ "pagelen").asOpt[Int],
                page = (json \ "page").asOpt[Int],
                next = (json \ "next").asOpt[String],
                previous = (json \ "previous").asOpt[String]
            )
          )
      case Left(error) => FailedResponse(error.detail)
    }
  }

  /**
    * Does a paginated API request and parses the json output into a sequence of classes.
    */
  def executePaginated[T: Reads](url: String): RequestResponse[Seq[T]] = {
    val FIRST_PAGE = 1

    def extractValues(json: JsValue): RequestResponse[Seq[T]] =
      (json \ "values")
        .validate[Seq[T]]
        .fold(e => FailedResponse(s"Failed to parse json ($e): $json"), a => SuccessfulResponse(a))

    get(url) match {
      case Right(json) =>
        val nextPages = (for {
          size <- (json \ "size").asOpt[Double]
          pagelen <- (json \ "pagelen").asOpt[Double]
        } yield {
          val lastPage = math.ceil(size / pagelen).toInt
          new ParRange(FIRST_PAGE + 1 to lastPage).map { page =>
            val nextUrl = new URI(url).addQuery(s"page=$page").toString
            get(nextUrl) match {
              case Right(nextJson) => extractValues(nextJson)
              case Left(error) => FailedResponse(error.detail)
            }
          }
        }).getOrElse(Seq(SuccessfulResponse(Seq.empty)))

        val values = extractValues(json)

        (Seq(values) ++ nextPages).foldLeft[RequestResponse[Seq[T]]](SuccessfulResponse(Seq.empty[T])) { (a, b) =>
          RequestResponse.applyDiscardingPaginationInfo(a, b)
        }

      case Left(error) =>
        FailedResponse(error.detail)
    }
  }

  private def performRequest[D: BodyWriteable, T: Reads](method: String, url: String, values: D): RequestResponse[T] =
    withClientRequest { client =>
      val builtRequest = client
        .url(url)
        .authenticate(authenticator)
        .withFollowRedirects(follow = true)
        .withMethod(method)
        .withBody(values)

      val result = Await.result(builtRequest.execute(), requestTimeout)

      val value = if (Seq(HTTPStatusCodes.OK, HTTPStatusCodes.CREATED).contains(result.status)) {
        parseJson(result.body) match {
          case Right(json) =>
            json.validate[T] match {
              case s: JsSuccess[T] =>
                SuccessfulResponse(s.value)
              case e: JsError =>
                val msg =
                  s"""
                   |Failed to validate json:
                   |$json
                   |JsError errors:
                   |${e.errors.mkString(System.lineSeparator)}
                """.stripMargin
                FailedResponse(msg)
            }
          case Left(message) =>
            FailedResponse(message.detail)
        }
      } else {
        FailedResponse(result.statusText)
      }

      value
    }

  def getJson[T: Reads](url: String): RequestResponse[T] =
    performRequest("GET", url, JsNull)

  def postForm[D: BodyWriteable, T: Reads](url: String, values: D): RequestResponse[T] =
    performRequest("POST", url, values)

  def postJson[T: Reads](url: String, values: JsValue): RequestResponse[T] =
    performRequest("POST", url, values)

  def putForm[T: Reads](url: String, values: Map[String, Seq[String]]): RequestResponse[T] =
    performRequest("PUT", url, values)

  def putJson[T: Reads](url: String, values: JsValue): RequestResponse[T] =
    performRequest("PUT", url, values)

  /* copy paste from post ... */
  def delete[T](url: String): RequestResponse[Boolean] = withClientRequest { client =>
    val builtRequest = client
      .url(url)
      .authenticate(authenticator)
      .withFollowRedirects(follow = true)

    val result = Await.result(builtRequest.delete(), requestTimeout)

    val value =
      if (Seq(HTTPStatusCodes.OK, HTTPStatusCodes.CREATED, HTTPStatusCodes.NO_CONTENT).contains(result.status)) {
        SuccessfulResponse(true)
      } else {
        FailedResponse(result.statusText)
      }

    value
  }

  private def get(url: String): Either[ResponseError, JsValue] = withClientEither { client =>
    val builtRequest = client
      .url(url)
      .authenticate(authenticator)
      .withFollowRedirects(follow = true)

    val result = Await.result(builtRequest.get(), requestTimeout)

    val value = if (result.status == HTTPStatusCodes.OK || result.status == HTTPStatusCodes.CREATED) {
      parseJson(result.body)
    } else {
      Left(ResponseError(java.util.UUID.randomUUID().toString, result.statusText, result.statusText))
    }

    value
  }

  private def parseJson(input: String): Either[ResponseError, JsValue] = {
    Try {
      val json = Json.parse(input)
      val errorOpt = (json \ "error").asOpt[ResponseError]

      errorOpt
        .map { error =>
          Left(error)
        }
        .getOrElse(Right(json))
    } match {
      case Success(jsValue) =>
        jsValue
      case Failure(e) =>
        Left(ResponseError("Failed to parse json", e.getStackTrace.mkString(System.lineSeparator), e.getMessage))
    }
  }

  private def withClientEither[T](block: WSClient => Either[ResponseError, T]): Either[ResponseError, T] = {
    Try(block(client)) match {
      case Success(res) => res
      case Failure(error) =>
        Left(ResponseError("Request failed", getFullStackTrace(error), error.getMessage))
    }
  }

  private def withClientRequest[T](block: WSClient => RequestResponse[T]): RequestResponse[T] =
    Try(block(client)) match {
      case Success(res) => res
      case Failure(error) =>
        val statusMessage =
          s"""
             |Failed request:
             |$error
             |${getFullStackTrace(error)}
          """.stripMargin
        FailedResponse(statusMessage)
    }

  private def getFullStackTrace(throwableOpt: Throwable, accumulator: String = ""): String =
    Option(throwableOpt)
      .map { throwable =>
        val newAccumulator = s"$accumulator${Properties.lineSeparator}${throwable.getStackTrace.mkString("", EOL, EOL)}"
        getFullStackTrace(throwable.getCause, newAccumulator)
      }
      .getOrElse(accumulator)

  def getRaw(url: String): RequestResponse[String] =
    withClientEither { client =>
      val builtRequest = client
        .url(url)
        .authenticate(authenticator)
        .withFollowRedirects(follow = true)

      val result = Await.result(builtRequest.get(), requestTimeout)

      if (result.status == HTTPStatusCodes.OK || result.status == HTTPStatusCodes.CREATED) {
        Right(result.body)
      } else {
        Left(ResponseError(java.util.UUID.randomUUID().toString, result.statusText, result.statusText))
      }
    } match {
      case Right(response) => SuccessfulResponse(response)
      case Left(error) => FailedResponse(error.detail)
    }
}
