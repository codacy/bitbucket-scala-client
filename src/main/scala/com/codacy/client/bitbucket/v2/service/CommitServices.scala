package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v2.{CommitComment, SimpleCommit, SimpleCommitSha}
import play.api.libs.json.{JsNumber, JsObject, JsString, Json}

class CommitServices(client: BitbucketClient) {

  def getCommit(author: String, repository: String, commit: String): RequestResponse[SimpleCommit] =
    client.execute(Request(generateCommitUrl(author, repository, commit), classOf[SimpleCommit]))

  def createComment(
      author: String,
      repository: String,
      commit: String,
      body: String,
      file: Option[String] = None,
      line: Option[Int] = None
  ): RequestResponse[CommitComment] = {
    val commitCommentUrl = generateCommentUrl(author, repository, commit)

    val params = for {
      filename <- file
      lineTo <- line
    } yield {
      "inline" -> Json.obj("path" -> JsString(filename), "to" -> JsNumber(lineTo))
    }

    val values = JsObject(params.toSeq :+ "content" -> Json.obj("raw" -> JsString(body)))

    client.postJson(Request(commitCommentUrl, classOf[CommitComment]), values)
  }

  def listComments(author: String, repository: String, commit: String): RequestResponse[Seq[CommitComment]] =
    client
      .executePaginated(Request(generateCommentUrl(author, repository, commit), classOf[Seq[CommitComment]]))
      .map(_.filterNot(_.deleted))

  def deleteComment(author: String, repository: String, commit: String, commentId: Long): RequestResponse[Boolean] =
    client.delete(generateCommentUrl(author, repository, commit, commentId.toString))

  // https://developer.atlassian.com/cloud/bitbucket/rest/api-group-commits/#api-repositories-workspace-repo-slug-diff-spec-get
  def getCommitDiff(
      workspace: String,
      repository: String,
      fromCommitSha: String,
      toCommitSha: String
  ): RequestResponse[String] =
    client.getRaw(s"${client.repositoriesBaseUrl}/$workspace/$repository/diff/$fromCommitSha..$toCommitSha")

  def getCommitSha(workspace: String, repository: String, commitSha: String): RequestResponse[SimpleCommitSha] =
    client.execute(Request(generateCommitUrl(workspace, repository, commitSha), classOf[SimpleCommitSha]))

  private def generateCommitUrl(author: String, repository: String, commitSha: String, paths: String*): String = {
    val encodedAuthor = URLEncoder.encode(author, "UTF-8")
    val encodedRepository = URLEncoder.encode(repository, "UTF-8")
    val encodedCommit = URLEncoder.encode(commitSha, "UTF-8")
    val encodedPaths = if (paths.isEmpty) "" else "/" + paths.map(URLEncoder.encode(_, "UFT-8")).mkString("/")

    s"${client.repositoriesBaseUrl}/$encodedAuthor/$encodedRepository/commit/$encodedCommit$encodedPaths"
  }

  private def generateCommentUrl(author: String, repository: String, commit: String, extraPaths: String*): String =
    generateCommitUrl(author, repository, commit, "comments" +: extraPaths: _*)
}
