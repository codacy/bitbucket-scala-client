package com.codacy.client.bitbucket.v1.service

import com.codacy.client.bitbucket.v1.{PullRequestComment, SimplePullRequestComment}
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.util.CommitHelper
import play.api.libs.json._

class PullRequestServices(client: BitbucketClient) {

  private[this] def postNewComment(
      author: String,
      repo: String,
      prId: Int,
      values: JsObject
  ): RequestResponse[PullRequestComment] = {
    val url = s"https://bitbucket.org/api/1.0/repositories/$author/$repo/pullrequests/$prId/comments"
    client.postJson(Request(url, classOf[PullRequestComment]), values)
  }

  def createPullRequestComment(
      author: String,
      repo: String,
      prId: Int,
      content: String
  ): RequestResponse[PullRequestComment] = {
    val values = Json.obj("content" -> JsString(content))
    postNewComment(author, repo, prId, values)
  }

  def createLineComment(
      author: String,
      repo: String,
      prId: Int,
      commitUUID: String,
      body: String,
      file: Option[String],
      line: Option[Int]
  ): RequestResponse[PullRequestComment] = {
    val params = file.map(filename => "filename" -> JsString(filename)) ++
      line.map(lineTo => "line_to" -> JsNumber(lineTo))

    val values = JsObject(
      params.toSeq :+ "content" -> JsString(body) :+ "anchor" -> JsString(CommitHelper.anchor(commitUUID))
    )
    postNewComment(author, repo, prId, values = values)
  }

  def deleteComment(author: String, repo: String, commitUUID: String, pullRequestId: Int, commentId: Long): Unit = {
    val url =
      s"https://bitbucket.org/api/1.0/repositories/$author/$repo/pullrequests/$pullRequestId/comments/$commentId"

    client.delete(url)
  }

  def listComments(author: String, repo: String, pullRequestId: Int): RequestResponse[Seq[SimplePullRequestComment]] = {
    val url = s"https://bitbucket.org/api/1.0/repositories/$author/$repo/pullrequests/$pullRequestId/comments"

    client.execute(Request(url, classOf[Seq[SimplePullRequestComment]]))
  }

}
