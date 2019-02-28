package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.v2.CommitComment
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.{JsNumber, JsObject, JsString, Json}
import com.codacy.client.bitbucket.client.Authentication.Credentials
import com.codacy.client.bitbucket.client.BitbucketAsyncClient
import play.api.libs.ws.ning.NingWSClient
import scala.concurrent.Future

class CommitServices(client: BitbucketClient) {

  def createComment(
      author: String,
      repo: String,
      commit: String,
      body: String,
      file: Option[String] = None,
      line: Option[Int] = None
  ): RequestResponse[CommitComment] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$author/$repo/commit/$commit/comments"

    val params = for {
      filename <- file
      lineTo <- line
    } yield {
      "inline" -> Json.obj("path" -> JsString(filename), "to" -> JsNumber(lineTo))
    }

    val values = JsObject(params.toSeq :+ "content" -> Json.obj("raw" -> JsString(body)))

    client.postJson(Request(url, classOf[CommitComment]), values)
  }

  def listComments(author: String, repo: String, commit: String): RequestResponse[Seq[CommitComment]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$author/$repo/commit/$commit/comments"

    client
      .executePaginated(Request(url, classOf[Seq[CommitComment]]))
      .map(_.filterNot(_.deleted))
  }

  def deleteComment(author: String, repo: String, commit: String, commentId: Long): RequestResponse[Boolean] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$author/$repo/commit/$commit/comments/$commentId"

    client.delete(url)
  }
}

class AsyncCommitServices(client: BitbucketAsyncClient) {

  def createComment(
      author: String,
      repo: String,
      commit: String,
      body: String,
      file: Option[String] = None,
      line: Option[Int] = None
  )(credentials: Credentials)(implicit nc: NingWSClient): Future[RequestResponse[CommitComment]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$author/$repo/commit/$commit/comments"

    val params = for {
      filename <- file
      lineTo <- line
    } yield {
      "inline" -> Json.obj("path" -> JsString(filename), "to" -> JsNumber(lineTo))
    }

    val values = JsObject(params.toSeq :+ "content" -> Json.obj("raw" -> JsString(body)))

    client.postJson(Request(url, classOf[CommitComment]), values, credentials)
  }

  def listComments(author: String, repo: String, commit: String)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[Seq[CommitComment]]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$author/$repo/commit/$commit/comments"

    import client.ec

    client
      .executePaginated(Request(url, classOf[Seq[CommitComment]]), credentials)
      .map(_.map(_.filterNot(_.deleted)))
  }

  def deleteComment(author: String, repo: String, commit: String, commentId: Long)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[Boolean]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$author/$repo/commit/$commit/comments/$commentId"

    client.delete(url, credentials)
  }
}
