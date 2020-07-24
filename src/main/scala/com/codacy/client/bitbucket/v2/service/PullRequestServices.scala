package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v2.{PullRequest, PullRequestComment, PullRequestReviewers, SimpleCommit}
import play.api.libs.json._

class PullRequestServices(client: BitbucketClient) {

  /*
   * Gets the list of a repository pull requests
   *
   * States: OPEN | MERGED | DECLINED
   *
   */
  def getPullRequests(
      owner: String,
      repository: String,
      states: Seq[String] = Seq("OPEN"),
      size: Int = 50
  ): RequestResponse[Seq[PullRequest]] = {
    val pullRequestsUrl = generatePullRequestsUrl(owner, repository)
    val encodedStates = states.map(state => URLEncoder.encode(state, "UTF-8"))
    val url = s"$pullRequestsUrl?pagelen=$size&state=${encodedStates.mkString("&state=")}"

    client.executePaginated(Request(url, classOf[Seq[PullRequest]]))
  }

  /*
   * Gets the list of commits of a pull request
   *
   */
  def getPullRequestCommits(
      owner: String,
      repository: String,
      prId: Long,
      size: Int = 100
  ): RequestResponse[Seq[SimpleCommit]] = {
    val pullRequestsUrl = generatePullRequestsUrl(owner, repository)
    val url = s"$pullRequestsUrl/$prId/commits?pagelen=$size"

    client.executePaginated(Request(url, classOf[Seq[SimpleCommit]]))
  }

  private[this] def postNewComment(
      owner: String,
      repository: String,
      prId: Int,
      values: JsObject
  ): RequestResponse[PullRequestComment] = {
    val pullRequestsUrl = generatePullRequestsUrl(owner, repository)
    val url = s"$pullRequestsUrl/$prId/comments"
    client.postJson(Request(url, classOf[PullRequestComment]), values)
  }

  def create(
      owner: String,
      repository: String,
      title: String,
      sourceBranch: String,
      destinationBranch: String
  ): RequestResponse[JsObject] = {
    val pullRequestsUrl = generatePullRequestsUrl(owner, repository)

    val payload = Json.obj(
      "title" -> title,
      "source" -> Json.obj("branch" -> Json.obj("name" -> sourceBranch)),
      "destination" -> Json.obj("branch" -> Json.obj("name" -> destinationBranch))
    )

    client.postJson(Request(pullRequestsUrl, classOf[JsObject]), payload)
  }

  def postApprove(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val pullRequestsUrl = generatePullRequestsUrl(owner, repository)
    val url = s"$pullRequestsUrl/$prId/approve"
    client.postJson(Request(url, classOf[JsObject]), JsNull)
  }

  def deleteApprove(owner: String, repository: String, prId: Long): RequestResponse[Boolean] = {
    val pullRequestsUrl = generatePullRequestsUrl(owner, repository)
    val url = s"$pullRequestsUrl/$prId/approve"
    client.delete(url)
  }

  def merge(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val pullRequestsUrl = generatePullRequestsUrl(owner, repository)
    val url = s"$pullRequestsUrl/$prId/merge"
    client.postJson(Request(url, classOf[JsObject]), JsNull)
  }

  def decline(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val pullRequestsUrl = generatePullRequestsUrl(owner, repository)
    val url = s"$pullRequestsUrl/$prId/decline"
    client.postJson(Request(url, classOf[JsObject]), JsNull)
  }

  def createLineComment(
      author: String,
      repo: String,
      prId: Int,
      body: String,
      file: Option[String],
      line: Option[Int]
  ): RequestResponse[PullRequestComment] = {
    val params = for {
      filename <- file
      lineTo <- line
    } yield {
      "inline" -> Json.obj("path" -> JsString(filename), "to" -> JsNumber(lineTo))
    }

    val values = JsObject(params.toSeq :+ "content" -> Json.obj("raw" -> JsString(body)))
    postNewComment(author, repo, prId, values)
  }

  def createPullRequestComment(
      author: String,
      repo: String,
      prId: Int,
      content: String
  ): RequestResponse[PullRequestComment] = {
    val values = Json.obj("content" -> Json.obj("raw" -> JsString(content)))
    postNewComment(author, repo, prId, values)
  }

  def deleteComment(
      author: String,
      repository: String,
      pullRequestId: Int,
      commentId: Long
  ): RequestResponse[Boolean] = {
    val pullRequestsUrl = generatePullRequestsUrl(author, repository)
    val url = s"$pullRequestsUrl/$pullRequestId/comments/$commentId"

    client.delete(url)
  }

  def listComments(author: String, repository: String, pullRequestId: Int): RequestResponse[Seq[PullRequestComment]] = {
    val pullRequestsUrl = generatePullRequestsUrl(author, repository)
    val url = s"$pullRequestsUrl/$pullRequestId/comments"

    client
      .executePaginated(Request(url, classOf[Seq[PullRequestComment]]))
      .map(_.filterNot(_.deleted))
  }

  def getPullRequestsReviewers(owner: String, repository: String, prId: Long): RequestResponse[PullRequestReviewers] = {
    val pullRequestsUrl = generatePullRequestsUrl(owner, repository)
    val url = s"$pullRequestsUrl/$prId"

    client.execute(Request(url, classOf[PullRequestReviewers]))
  }

  private def generatePullRequestsUrl(owner: String, repo: String): String = {
    val encodedOwner = URLEncoder.encode(owner, "UTF-8")
    val encodedRepo = URLEncoder.encode(repo, "UTF-8")
    s"${client.repositoriesBaseUrl}/$encodedOwner/$encodedRepo/pullrequests"
  }

}
