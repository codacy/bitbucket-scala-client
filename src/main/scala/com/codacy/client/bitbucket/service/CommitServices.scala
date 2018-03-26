package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.CommitComment
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.util.CommitHelper
import play.api.libs.json.{JsNumber, JsObject, JsString}

class CommitServices(client: BitbucketClient) {

  def createComment(author: String, repo: String, commit: String, body: String, file: Option[String] = None, line: Option[Int] = None): RequestResponse[CommitComment] = {
    val url = s"https://bitbucket.org/api/1.0/repositories/$author/$repo/changesets/${CommitHelper.anchor(commit)}/comments"

    val params = file.map(filename => "filename" -> JsString(filename)) ++
      line.map(lineTo => "line_to" -> JsNumber(lineTo))

    val values = JsObject(params.toSeq :+ "content" -> JsString(body))

    client.postJson(Request(url, classOf[CommitComment]), values)
  }

  def deleteComment(author: String, repo: String, commit: String, commentId: Long): Unit = {
    val url = s"https://bitbucket.org/api/1.0/repositories/$author/$repo/changesets/${CommitHelper.anchor(commit)}/comments/$commentId"

    client.delete(url)
  }
}
