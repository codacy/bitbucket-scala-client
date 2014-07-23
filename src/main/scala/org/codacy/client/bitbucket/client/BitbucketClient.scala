package org.codacy.client.bitbucket.client

import com.ning.http.client.AsyncHttpClient
import org.codacy.client.bitbucket.util.HTTPStatusCodes
import play.api.libs.json.{JsValue, Json, Reads}
import play.api.libs.oauth._
import play.api.libs.ws.WSClient
import play.api.libs.ws.ning.NingWSClient

import scala.concurrent.Await
import scala.concurrent.duration._

class BitbucketClient(key: String, secretKey: String, token: String, secretToken: String) {

  private lazy val KEY = ConsumerKey(key, secretKey)
  private lazy val TOKEN = RequestToken(token, secretToken)

  private lazy val BITBUCKET = OAuth(ServiceInfo(
    "https://bitbucket.org/!api/1.0/oauth/request_token",
    "https://bitbucket.org/!api/1.0/oauth/access_token",
    "https://bitbucket.org/!api/1.0/oauth/authenticate", KEY),
    use10a = true)

  /*
   * Does an API request and parses the json output into a class
   */
  def execute[T](request: Request[T])(implicit reader: Reads[T]): RequestResponse[T] = {
    get(request.url) match {
      case Right(json) => RequestResponse(json.asOpt[T])
      case Left(error) => RequestResponse(None, error.detail, hasError = true)
    }
  }

  /*
   * Does an paginated API request and parses the json output into a sequence of classes
   */
  def executePaginated[T](request: Request[Seq[T]])(implicit reader: Reads[T]): RequestResponse[Seq[T]] = {
    get(request.url) match {
      case Right(json) =>
        val nextPage = (json \ "next").asOpt[String]
        val nextRepos = nextPage.map {
          nextUrl =>
            executePaginated(Request(nextUrl, request.classType)).value.getOrElse(Seq())
        }.getOrElse(Seq())

        RequestResponse(Some((json \ "values").as[Seq[T]] ++ nextRepos))

      case Left(error) =>
        RequestResponse[Seq[T]](None, error.detail, hasError = true)
    }
  }

  /*
   * Does an API post
   */
  def post[T](request: Request[T], values: Map[String, Seq[String]])(implicit reader: Reads[T]): RequestResponse[T] = {
    val client: WSClient = new NingWSClient(new AsyncHttpClient().getConfig)

    val jpromise = client.url(request.url).sign(OAuthCalculator(KEY, TOKEN)).withFollowRedirects(follow = true).post(values)
    val result = Await.result(jpromise, Duration(10, SECONDS))

    if (Seq(HTTPStatusCodes.OK, HTTPStatusCodes.CREATED).contains(result.status)) {
      val body = result.body

      /* TODO: remove this when not needed (only keep for debug purposes) */
      //    println("\n\n")
      //    println(s"STATUS: ${result.status}")
      //    println("\n\n")
      //    println(body)
      //    println("\n\n")

      val jsValue = parseJson(body)
      if (jsValue.isRight) {
        jsValue.right.get.as[T]
        RequestResponse(jsValue.right.get.asOpt[T])
      } else {
        RequestResponse(None, message = jsValue.left.get.detail, hasError = true)
      }
    } else {
      RequestResponse(None, result.statusText, hasError = true)
    }
  }

  private def get(url: String): Either[ResponseError, JsValue] = {
    val client: WSClient = new NingWSClient(new AsyncHttpClient().getConfig)

    val jpromise = client.url(url).sign(OAuthCalculator(KEY, TOKEN)).withFollowRedirects(follow = true).get()
    val result = Await.result(jpromise, Duration(10, SECONDS))

    if (Seq(HTTPStatusCodes.OK, HTTPStatusCodes.CREATED).contains(result.status)) {
      val body = result.body

      /* TODO: remove this when not needed (only keep for debug purposes) */
      //      println("\n\n")
      //      println(s"STATUS: ${result.status}")
      //      println("\n\n")
      //      println(body)
      //      println("\n\n")

      parseJson(body)
    } else {
      Left(ResponseError(java.util.UUID.randomUUID().toString, result.statusText, result.statusText))
    }
  }

  private def parseJson(input: String): Either[ResponseError, JsValue] = {
    val json = Json.parse(input)

    val errorOpt = (json \ "error").asOpt[ResponseError]

    errorOpt.map {
      error =>
        Left(error)
    }.getOrElse(Right(json))
  }
}
