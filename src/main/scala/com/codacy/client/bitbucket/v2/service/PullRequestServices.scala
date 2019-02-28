package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.v2.{PullRequest, PullRequestComment, PullRequestReviewers, SimpleCommit}
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json._
import com.codacy.client.bitbucket.client.Authentication.Credentials
import com.codacy.client.bitbucket.client.BitbucketAsyncClient
import play.api.libs.ws.ning.NingWSClient
import scala.concurrent.Future

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
      states: Seq[String] = Seq("OPEN")
  ): RequestResponse[Seq[PullRequest]] = {
    val url =
      s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests?pagelen=50&state=${states.mkString("&state=")}"

    client.executePaginated(Request(url, classOf[Seq[PullRequest]]))
  }

  /*
   * Gets the list of commits of a pull request
   *
   */
  def getPullRequestCommits(owner: String, repository: String, prId: Long): RequestResponse[Seq[SimpleCommit]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId/commits?pagelen=100"

    client.executePaginated(Request(url, classOf[Seq[SimpleCommit]]))
  }

  private[this] def postNewComment(
      owner: String,
      repo: String,
      prId: Int,
      values: JsObject
  ): RequestResponse[PullRequestComment] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repo/pullrequests/$prId/comments"
    client.postJson(Request(url, classOf[PullRequestComment]), values)
  }

  def create(
      owner: String,
      repository: String,
      title: String,
      sourceBranch: String,
      destinationBranch: String
  ): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests"

    val payload = Json.obj(
      "title" -> title,
      "source" -> Json.obj("branch" -> Json.obj("name" -> sourceBranch)),
      "destination" -> Json.obj("branch" -> Json.obj("name" -> destinationBranch))
    )

    client.postJson(Request(url, classOf[JsObject]), payload)
  }

  def postApprove(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId/approve"
    client.postJson(Request(url, classOf[JsObject]), JsNull)
  }

  def deleteApprove(owner: String, repository: String, prId: Long): RequestResponse[Boolean] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId/approve"
    client.delete(url)
  }

  def merge(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId/merge"
    client.postJson(Request(url, classOf[JsObject]), JsNull)
  }

  def decline(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId/decline"
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

  def deleteComment(author: String, repo: String, pullRequestId: Int, commentId: Long): RequestResponse[Boolean] = {
    val url =
      s"https://bitbucket.org/api/2.0/repositories/$author/$repo/pullrequests/$pullRequestId/comments/$commentId"

    client.delete(url)
  }

  def listComments(author: String, repo: String, pullRequestId: Int): RequestResponse[Seq[PullRequestComment]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$author/$repo/pullrequests/$pullRequestId/comments"

    client
      .executePaginated(Request(url, classOf[Seq[PullRequestComment]]))
      .map(_.filterNot(_.deleted))
  }

  def getPullRequestsReviewers(owner: String, repository: String, prId: Long): RequestResponse[PullRequestReviewers] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId"

    client.execute(Request(url, classOf[PullRequestReviewers]))
  }

}

class AsyncPullRequestServices(client: BitbucketAsyncClient) {

  /*
   * Gets the list of a repository pull requests
   *
   * States: OPEN | MERGED | DECLINED
   *
   */
  def getPullRequests(owner: String, repository: String, states: Seq[String] = Seq("OPEN"))(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[Seq[PullRequest]]] = {
    val url =
      s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests?pagelen=50&state=${states.mkString("&state=")}"

    client.executePaginated(Request(url, classOf[Seq[PullRequest]]), credentials)
  }

  /*
   * Gets the list of commits of a pull request
   *
   */
  def getPullRequestCommits(owner: String, repository: String, prId: Long)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[Seq[SimpleCommit]]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId/commits?pagelen=100"

    client.executePaginated(Request(url, classOf[Seq[SimpleCommit]]), credentials)
  }

  private[this] def postNewComment(owner: String, repo: String, prId: Int, values: JsObject)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[PullRequestComment]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repo/pullrequests/$prId/comments"
    client.postJson(Request(url, classOf[PullRequestComment]), values, credentials)
  }

  def create(owner: String, repository: String, title: String, sourceBranch: String, destinationBranch: String)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[JsObject]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests"

    val payload = Json.obj(
      "title" -> title,
      "source" -> Json.obj("branch" -> Json.obj("name" -> sourceBranch)),
      "destination" -> Json.obj("branch" -> Json.obj("name" -> destinationBranch))
    )

    client.postJson(Request(url, classOf[JsObject]), payload, credentials)
  }

  def postApprove(owner: String, repository: String, prId: Long)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[JsObject]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId/approve"
    client.postJson(Request(url, classOf[JsObject]), JsNull, credentials)
  }

  def deleteApprove(owner: String, repository: String, prId: Long)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[Boolean]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId/approve"
    client.delete(url, credentials)
  }

  def merge(owner: String, repository: String, prId: Long)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[JsObject]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId/merge"
    client.postJson(Request(url, classOf[JsObject]), JsNull, credentials)
  }

  def decline(owner: String, repository: String, prId: Long)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[JsObject]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId/decline"
    client.postJson(Request(url, classOf[JsObject]), JsNull, credentials)
  }

  def createLineComment(author: String, repo: String, prId: Int, body: String, file: Option[String], line: Option[Int])(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[PullRequestComment]] = {
    val params = for {
      filename <- file
      lineTo <- line
    } yield {
      "inline" -> Json.obj("path" -> JsString(filename), "to" -> JsNumber(lineTo))
    }

    val values = JsObject(params.toSeq :+ "content" -> Json.obj("raw" -> JsString(body)))
    postNewComment(author, repo, prId, values)(credentials)
  }

  def createPullRequestComment(author: String, repo: String, prId: Int, content: String)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[PullRequestComment]] = {
    val values = Json.obj("content" -> Json.obj("raw" -> JsString(content)))
    postNewComment(author, repo, prId, values)(credentials)
  }

  def deleteComment(author: String, repo: String, pullRequestId: Int, commentId: Long)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[Boolean]] = {
    val url =
      s"https://bitbucket.org/api/2.0/repositories/$author/$repo/pullrequests/$pullRequestId/comments/$commentId"

    client.delete(url, credentials)
  }

  def listComments(author: String, repo: String, pullRequestId: Int)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[Seq[PullRequestComment]]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$author/$repo/pullrequests/$pullRequestId/comments"
    import client.ec

    client
      .executePaginated(Request(url, classOf[Seq[PullRequestComment]]), credentials)
      .map(_.map(_.filterNot(_.deleted)))
  }

  def getPullRequestsReviewers(owner: String, repository: String, prId: Long)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[PullRequestReviewers]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/pullrequests/$prId"

    client.execute(Request(url, classOf[PullRequestReviewers]), credentials)
  }

}
