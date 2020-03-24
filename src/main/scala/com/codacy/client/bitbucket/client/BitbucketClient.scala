package com.codacy.client.bitbucket.client

import java.net.URI

import com.codacy.client.bitbucket.BodyWriteableWrapper.BodyWriteable
import com.codacy.client.bitbucket.client.Authentication._
import com.codacy.client.bitbucket.util.HTTPStatusCodes
import com.codacy.client.bitbucket.util.Implicits.URIQueryParam
import play.api.libs.json._
import play.api.libs.ws.WSClient

import scala.compat.Platform.EOL
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}
import scala.util.{Failure, Properties, Success, Try}

class BitbucketClient(credentials: Credentials)(wsBuilder: () => WSClient) {

  private lazy val requestTimeout = Duration(10, SECONDS)

  private lazy val authenticator = Authenticator.fromCredentials(credentials)

  /*
   * Does an API request and parses the json output into a class
   */
  def execute[T](request: Request[T])(implicit reader: Reads[T]): RequestResponse[T] = {
    get(request.url) match {
      case Right(json) =>
        json.validate[T].fold(e => FailedResponse(s"Failed to parse json ($e): $json"), a => SuccessfulResponse(a))
      case Left(error) => FailedResponse(error.detail)
    }
  }

  /*
   * Does a paginated API request and parses the json output into a sequence of classes
   */
  def executePaginated[T](request: Request[Seq[T]])(implicit reader: Reads[T]): RequestResponse[Seq[T]] = {
    val FIRST_PAGE = 1

    def extractValues(json: JsValue): RequestResponse[Seq[T]] =
      (json \ "values")
        .validate[Seq[T]]
        .fold(e => FailedResponse(s"Failed to parse json ($e): $json"), a => SuccessfulResponse(a))

    get(request.url) match {
      case Right(json) =>
        val nextPages = (for {
          size <- (json \ "size").asOpt[Double]
          pagelen <- (json \ "pagelen").asOpt[Double]
        } yield {
          val lastPage = math.ceil(size / pagelen).toInt
          (FIRST_PAGE + 1 to lastPage).par
            .map { page =>
              val nextUrl = new URI(request.url).addQuery(s"page=$page").toString
              get(nextUrl) match {
                case Right(nextJson) => extractValues(nextJson)
                case Left(error) => FailedResponse(error.detail)
              }
            }
            .to[Seq]
        }).getOrElse(Seq(SuccessfulResponse(Seq.empty)))

        val values = extractValues(json)

        (values +: nextPages).foldLeft[RequestResponse[Seq[T]]](SuccessfulResponse(Seq.empty[T])) { (a, b) =>
          RequestResponse.apply(a, b)
        }

      case Left(error) =>
        FailedResponse(error.detail)
    }
  }

  /*
   * Does an API request
   */
  private def performRequest[D, T](method: String, request: Request[T], values: D)(
      implicit reader: Reads[T],
      writer: BodyWriteable[D]
  ): RequestResponse[T] = withClientRequest { client =>
    val jpromise = client
      .url(request.url)
      .authenticate(authenticator)
      .withFollowRedirects(follow = true)
      .withMethod(method)
      .withBody(values)
      .execute()
    val result = Await.result(jpromise, requestTimeout)

    val value = if (Seq(HTTPStatusCodes.OK, HTTPStatusCodes.CREATED).contains(result.status)) {
      val body = result.body

      val jsValue = parseJson(body)
      jsValue match {
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

  def postForm[D, T](
      request: Request[T],
      values: D
  )(implicit reader: Reads[T], writer: BodyWriteable[D]): RequestResponse[T] = {
    performRequest("POST", request, values)
  }

  def postJson[T](request: Request[T], values: JsValue)(implicit reader: Reads[T]): RequestResponse[T] = {
    performRequest("POST", request, values)
  }

  def putForm[T](request: Request[T], values: Map[String, Seq[String]])(
      implicit reader: Reads[T]
  ): RequestResponse[T] = {
    performRequest("PUT", request, values)
  }

  def putJson[T](request: Request[T], values: JsValue)(implicit reader: Reads[T]): RequestResponse[T] = {
    performRequest("PUT", request, values)
  }

  /* copy paste from post ... */
  def delete[T](url: String): RequestResponse[Boolean] = withClientRequest { client =>
    val jpromise = client
      .url(url)
      .authenticate(authenticator)
      .withFollowRedirects(follow = true)
      .delete()
    val result = Await.result(jpromise, requestTimeout)

    val value =
      if (Seq(HTTPStatusCodes.OK, HTTPStatusCodes.CREATED, HTTPStatusCodes.NO_CONTENT).contains(result.status)) {
        SuccessfulResponse(true)
      } else {
        FailedResponse(result.statusText)
      }

    value
  }

  private def get(url: String): Either[ResponseError, JsValue] = withClientEither { client =>
    val jpromise = client
      .url(url)
      .authenticate(authenticator)
      .withFollowRedirects(follow = true)
      .get()
    val result = Await.result(jpromise, requestTimeout)

    val value = if (Seq(HTTPStatusCodes.OK, HTTPStatusCodes.CREATED).contains(result.status)) {
      val body = result.body
      parseJson(body)
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
    withClient(block) match {
      case Success(res) => res
      case Failure(error) =>
        Left(ResponseError("Request failed", getFullStackTrace(error), error.getMessage))
    }
  }

  private def withClientRequest[T](block: WSClient => RequestResponse[T]): RequestResponse[T] = {
    withClient(block) match {
      case Success(res) => res
      case Failure(error) =>
        val statusMessage =
          s"""
             |Failed request:
             |
             |${getFullStackTrace(error)}
          """.stripMargin
        FailedResponse(statusMessage)
    }
  }

  private def withClient[T](block: WSClient => T): Try[T] = {
    val client: WSClient = wsBuilder()
    val result = Try(block(client))
    client.close()
    result
  }

  private def getFullStackTrace(throwableOpt: Throwable, accumulator: String = ""): String = {
    Option(throwableOpt)
      .map { throwable =>
        val newAccumulator = s"$accumulator${Properties.lineSeparator}${throwable.getStackTrace.mkString("", EOL, EOL)}"
        getFullStackTrace(throwable.getCause, newAccumulator)
      }
      .getOrElse(accumulator)
  }

}
