package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.v2.CommitComment
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.{JsNumber, JsObject, JsString, Json}

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
