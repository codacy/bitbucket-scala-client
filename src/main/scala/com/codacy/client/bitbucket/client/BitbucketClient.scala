package com.codacy.client.bitbucket.client

import java.util.concurrent.{SynchronousQueue, ThreadPoolExecutor, TimeUnit}

import com.codacy.client.bitbucket.util.HTTPStatusCodes
import com.ning.http.client.AsyncHttpClientConfig
import play.api.http.{ContentTypeOf, Writeable}
import play.api.libs.json._
import play.api.libs.oauth._
import play.api.libs.ws.ning.{NingAsyncHttpClientConfigBuilder, NingWSClient}

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}
import scala.util.{Failure, Properties, Success, Try}

class BitbucketClient(key: String, secretKey: String, token: String, secretToken: String) {

  private lazy val KEY = ConsumerKey(key, secretKey)
  private lazy val TOKEN = RequestToken(token, secretToken)

  private lazy val requestTimeout = Duration(10, SECONDS)
  private lazy val requestSigner = OAuthCalculator(KEY, TOKEN)

  /*
   * Does an API request and parses the json output into a class
   */
  def execute[T](request: Request[T])(implicit reader: Reads[T]): RequestResponse[T] = {
    get(request.url) match {
      case Right(json) => json.validate[T].fold(_ => FailedResponse(s"Failed to parse json: $json"), a => SuccessfulResponse(a))
      case Left(error) => FailedResponse(error.detail)
    }
  }

  /*
   * Does a paginated API request and parses the json output into a sequence of classes
   */
  def executePaginated[T](request: Request[Seq[T]])(implicit reader: Reads[T]): RequestResponse[Seq[T]] = {
    get(request.url) match {
      case Right(json) =>
        val nextPage = (json \ "next").asOpt[String]
        val nextRepos = nextPage.map {
          nextUrl =>
            executePaginated(Request(nextUrl, request.classType))
        }.getOrElse(SuccessfulResponse(Seq.empty))

        val values = (json \ "values").validate[Seq[T]].fold(_ => FailedResponse(s"Failed to parse json: $json"), a => SuccessfulResponse(a))
        RequestResponse.apply(values, nextRepos)

      case Left(error) =>
        FailedResponse(error.detail)
    }
  }

  /*
   * Does an API request
   */
  private def performRequest[D, T](method: String, request: Request[T], values: D)(implicit reader: Reads[T], writer: Writeable[D], contentType: ContentTypeOf[D]): RequestResponse[T] = withClientRequest { client =>
    val jpromise = client.url(request.url)
      .sign(requestSigner)
      .withFollowRedirects(follow = true)
      .withMethod(method).withBody(values).execute()
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

  def postForm[T](request: Request[T], values: Map[String, Seq[String]])(implicit reader: Reads[T]): RequestResponse[T] = {
    performRequest("POST", request, values)
  }

  def postJson[T](request: Request[T], values: JsValue)(implicit reader: Reads[T]): RequestResponse[T] = {
    performRequest("POST", request, values)
  }

  def putForm[T](request: Request[T], values: Map[String, Seq[String]])(implicit reader: Reads[T]): RequestResponse[T] = {
    performRequest("PUT", request, values)
  }

  def putJson[T](request: Request[T], values: JsValue)(implicit reader: Reads[T]): RequestResponse[T] = {
    performRequest("PUT", request, values)
  }

  /* copy paste from post ... */
  def delete[T](url: String): RequestResponse[Boolean] = withClientRequest { client =>
    val jpromise = client.url(url)
      .sign(requestSigner)
      .withFollowRedirects(follow = true)
      .delete()
    val result = Await.result(jpromise, requestTimeout)

    val value = if (Seq(HTTPStatusCodes.OK, HTTPStatusCodes.CREATED, HTTPStatusCodes.NO_CONTENT).contains(result.status)) {
      SuccessfulResponse(true)
    } else {
      FailedResponse(result.statusText)
    }

    value
  }

  private def get(url: String): Either[ResponseError, JsValue] = withClientEither { client =>
    val jpromise = client.url(url)
      .sign(requestSigner)
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

      errorOpt.map {
        error =>
          Left(error)
      }.getOrElse(Right(json))
    } match {
      case Success(jsValue) =>
        jsValue
      case Failure(e) =>
        Left(ResponseError("Failed to parse json",e.getStackTrace.mkString(System.lineSeparator),e.getMessage))
    }
  }

  private def withClientEither[T](block: NingWSClient => Either[ResponseError, T]): Either[ResponseError, T] = {
    withClient(block) match {
      case Success(res) => res
      case Failure(error) =>
        Left(ResponseError("Request failed", getFullStackTrace(error), error.getMessage))
    }
  }

  private def withClientRequest[T](block: NingWSClient => RequestResponse[T]): RequestResponse[T] = {
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

  private def withClient[T](block: NingWSClient => T): Try[T] = {
    val config = new NingAsyncHttpClientConfigBuilder().build()
    val clientConfig = new AsyncHttpClientConfig.Builder(config)
      .setExecutorService(new ThreadPoolExecutor(5, 15, 30L, TimeUnit.SECONDS, new SynchronousQueue[Runnable]))
      .build()
    val client = new NingWSClient(clientConfig)
    val result = Try(block(client))
    client.close()
    result
  }

  private def getFullStackTrace(throwableOpt: Throwable, accumulator: String = ""): String = {
    Option(throwableOpt).map { throwable =>
      val newAccumulator = s"$accumulator${Properties.lineSeparator}${throwable.getStackTraceString}"
      getFullStackTrace(throwable.getCause, newAccumulator)
    }.getOrElse(accumulator)
  }

}
