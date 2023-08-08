package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v2.CommitComment
import play.api.libs.json.{JsNumber, JsObject, JsString, Json}

class CommitServices(client: BitbucketClient) {

  def createComment(
      author: String,
      repository: String,
      commit: String,
      body: String,
      file: Option[String] = None,
      line: Option[Int] = None
  ): RequestResponse[CommitComment] = {
    val commitCommentUrl = generateCommitCommentUrl(author, repository, commit)

    val params = for {
      filename <- file
      lineTo <- line
    } yield {
      "inline" -> Json.obj("path" -> JsString(filename), "to" -> JsNumber(lineTo))
    }

    val values = JsObject(params.toSeq :+ "content" -> Json.obj("raw" -> JsString(body)))

    client.postJson(Request(commitCommentUrl, classOf[CommitComment]), values)
  }

  def listComments(author: String, repository: String, commit: String): RequestResponse[Seq[CommitComment]] = {
    val commitCommentUrl = generateCommitCommentUrl(author, repository, commit)

    client
      .executePaginated(Request(commitCommentUrl, classOf[Seq[CommitComment]]))
      .map(_.filterNot(_.deleted))
  }

  def deleteComment(author: String, repository: String, commit: String, commentId: Long): RequestResponse[Boolean] = {
    val commitCommentUrl = generateCommitCommentUrl(author, repository, commit)
    val url = s"$commitCommentUrl/$commentId"

    client.delete(url)
  }

  // https://developer.atlassian.com/cloud/bitbucket/rest/api-group-commits/#api-repositories-workspace-repo-slug-diff-spec-get
  def getCommitDiff(
      workspace: String,
      repository: String,
      fromCommitSha: String,
      toCommitSha: String
  ): RequestResponse[String] = {
    client.getRaw(s"${client.repositoriesBaseUrl}/$workspace/$repository/diff/$fromCommitSha..$toCommitSha")
  }

  private def generateCommitCommentUrl(author: String, repository: String, commit: String): String = {
    val encodedAuthor = URLEncoder.encode(author, "UTF-8")
    val encodedRepository = URLEncoder.encode(repository, "UTF-8")
    val encodedCommit = URLEncoder.encode(commit, "UTF-8")
    s"${client.repositoriesBaseUrl}/$encodedAuthor/$encodedRepository/commit/$encodedCommit/comments"
  }
}
