package com.codacy.client.bitbucket.client

import java.net.URI

import com.codacy.client.bitbucket.client.Authentication._
import com.codacy.client.bitbucket.util.HTTPStatusCodes
import com.codacy.client.bitbucket.util.Implicits.URIQueryParam
import com.ning.http.client.AsyncHttpClientConfig
import play.api.http.Writeable
import play.api.libs.json._
import play.api.libs.ws.ning.{NingAsyncHttpClientConfigBuilder, NingWSClient}

import scala.compat.Platform.EOL
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.{Duration, SECONDS}
import scala.util.{Failure, Properties, Success, Try}

class BitbucketClient(credentials: Credentials) {

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
      writer: Writeable[D]
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
  )(implicit reader: Reads[T], writer: Writeable[D]): RequestResponse[T] = {
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
    val clientConfig = new AsyncHttpClientConfig.Builder(config).build()
    val client = new NingWSClient(clientConfig)
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

class BitbucketAsyncClient()(implicit protected[bitbucket] val ec: ExecutionContext) {

  def startClient() = {
    val config = new NingAsyncHttpClientConfigBuilder().build()
    val clientConfig = new AsyncHttpClientConfig.Builder(config).build()
    val client = new NingWSClient(clientConfig)
    client
  }

  private lazy val requestTimeout = Duration(10, SECONDS).toMillis

  /*
   * Does an API request and parses the json output into a class
   */
  def execute[T](
      request: Request[T],
      credentials: Credentials
  )(implicit reader: Reads[T], client: NingWSClient): Future[RequestResponse[T]] = {
    get(request.url, credentials).map {
      _ match {
        case Right(json) =>
          json.validate[T].fold(e => FailedResponse(s"Failed to parse json ($e): $json"), a => SuccessfulResponse(a))
        case Left(error) => FailedResponse(error.detail)
      }
    }
  }

  /*
   * Does a paginated API request and parses the json output into a sequence of classes
   */
  def executePaginated[T](
      request: Request[Seq[T]],
      credentials: Credentials
  )(implicit reader: Reads[T], client: NingWSClient): Future[RequestResponse[Seq[T]]] = {
    val FIRST_PAGE = 1

    def extractValues(json: JsValue): RequestResponse[Seq[T]] =
      (json \ "values")
        .validate[Seq[T]]
        .fold(e => FailedResponse(s"Failed to parse json ($e): $json"), a => SuccessfulResponse(a))

    get(request.url, credentials).flatMap {
      _ match {
        case Right(json) =>
          val nextPages = (for {
            size <- (json \ "size").asOpt[Double]
            pagelen <- (json \ "pagelen").asOpt[Double]
          } yield {
            val lastPage = math.ceil(size / pagelen).toInt
            Future.sequence(
              (FIRST_PAGE + 1 to lastPage)
                .map { page =>
                  val nextUrl = new URI(request.url).addQuery(s"page=$page").toString
                  get(nextUrl, credentials).map {
                    _ match {
                      case Right(nextJson) => extractValues(nextJson)
                      case Left(error) => FailedResponse(error.detail)
                    }
                  }
                }
            )
          }).getOrElse(Future.successful(Seq(SuccessfulResponse(Seq.empty))))

          nextPages.map { np =>
            val values = extractValues(json)
            (values +: np).foldLeft[RequestResponse[Seq[T]]](SuccessfulResponse(Seq.empty[T])) { (a, b) =>
              RequestResponse.apply(a, b)
            }
          }
        case Left(error) => Future.successful(FailedResponse(error.detail))
      }
    }
  }

  /*
   * Does an API request
   */
  private def performRequest[D, T](method: String, request: Request[T], values: D, credentials: Credentials)(
      implicit reader: Reads[T],
      writer: Writeable[D],
      client: NingWSClient
  ): Future[RequestResponse[T]] = withClientRequest { client =>
    client
      .url(request.url)
      .withRequestTimeout(requestTimeout)
      .authenticate(Authenticator.fromCredentials(credentials))
      .withFollowRedirects(follow = true)
      .withMethod(method)
      .withBody(values)
      .execute()
      .map { result =>
        if (Seq(HTTPStatusCodes.OK, HTTPStatusCodes.CREATED).contains(result.status)) {
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
      }
  }

  def postForm[D, T](
      request: Request[T],
      values: D,
      credentials: Credentials
  )(implicit reader: Reads[T], writer: Writeable[D], client: NingWSClient): Future[RequestResponse[T]] = {
    performRequest("POST", request, values, credentials)
  }

  def postJson[T](request: Request[T], values: JsValue, credentials: Credentials)(
      implicit reader: Reads[T],
      client: NingWSClient
  ): Future[RequestResponse[T]] = {
    performRequest("POST", request, values, credentials)
  }

  def putForm[T](request: Request[T], values: Map[String, Seq[String]], credentials: Credentials)(
      implicit reader: Reads[T],
      client: NingWSClient
  ): Future[RequestResponse[T]] = {
    performRequest("PUT", request, values, credentials)
  }

  def putJson[T](request: Request[T], values: JsValue, credentials: Credentials)(
      implicit reader: Reads[T],
      client: NingWSClient
  ): Future[RequestResponse[T]] = {
    performRequest("PUT", request, values, credentials)
  }

  /* copy paste from post ... */
  def delete[T](url: String, credentials: Credentials)(
      implicit client: NingWSClient
  ): Future[RequestResponse[Boolean]] = withClientRequest { client =>
    client
      .url(url)
      .withRequestTimeout(requestTimeout)
      .authenticate(Authenticator.fromCredentials(credentials))
      .withFollowRedirects(follow = true)
      .delete()
      .map { result =>
        if (Seq(HTTPStatusCodes.OK, HTTPStatusCodes.CREATED, HTTPStatusCodes.NO_CONTENT).contains(result.status)) {
          SuccessfulResponse(true)
        } else {
          FailedResponse(result.statusText)
        }
      }
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

  private def get(url: String, credentials: Credentials)(
      implicit client: NingWSClient
  ): Future[Either[ResponseError, JsValue]] =
    withThrowingClient { client =>
      client
        .url(url)
        .withRequestTimeout(requestTimeout)
        .authenticate(Authenticator.fromCredentials(credentials))
        .withFollowRedirects(follow = true)
        .get()
        .map { result =>
          if (Seq(HTTPStatusCodes.OK, HTTPStatusCodes.CREATED).contains(result.status)) {
            val body = result.body
            parseJson(body)
          } else {
            Left(ResponseError(java.util.UUID.randomUUID().toString, result.statusText, result.statusText))
          }
        }
        .recover {
          case error: ThrowableResponseError =>
            Left(ResponseError(error.id, error.detail, error.message))
        }
    }

  private def withThrowingClient[T](block: NingWSClient => Future[T])(implicit client: NingWSClient): Future[T] = {
    withClient(block).recover {
      case error => throw new ThrowableResponseError("Request failed", getFullStackTrace(error), error.getMessage)
    }
  }

  private def withClient[T](block: NingWSClient => Future[T])(implicit client: NingWSClient): Future[T] = {
    block(client)
  }

  private def withClientRequest[T](
      block: NingWSClient => Future[RequestResponse[T]]
  )(implicit client: NingWSClient): Future[RequestResponse[T]] = {
    withClient(block).recover {
      case error =>
        val statusMessage =
          s"""
              |Failed request:
              |
              |${getFullStackTrace(error)}
            """.stripMargin
        FailedResponse(statusMessage)
    }
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
