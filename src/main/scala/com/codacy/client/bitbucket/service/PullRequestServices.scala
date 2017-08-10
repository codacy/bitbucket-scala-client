package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.{PullRequest, PullRequestComment, SimpleCommit, SimplePullRequestComment}
import play.api.libs.json._

class PullRequestServices(client: BitbucketClient) {

  /*
   * Gets the list of a repository pull requests
   *
   * States: OPEN | MERGED | DECLINED
   *
   */
  def getPullRequests(owner: String, repository: String, states: Seq[String] = Seq("OPEN")): RequestResponse[Seq[PullRequest]] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests?pagelen=50&state=${states.mkString("&state=")}"

    client.executePaginated(Request(url, classOf[Seq[PullRequest]]))
  }

  /*
   * Gets the list of commits of a pull request
   *
   */
  def getPullRequestCommits(owner: String, repository: String, prId: Long): RequestResponse[Seq[SimpleCommit]] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/commits?pagelen=100"

    client.executePaginated(Request(url, classOf[Seq[SimpleCommit]]))
  }

  def create(owner: String, repository: String, title: String, sourceBranch: String, destinationBranch: String): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests"

    val payload = Json.obj(
      "title" -> title,
      "source" -> Json.obj(
        "branch" -> Json.obj(
          "name" -> sourceBranch
        )
      ),
      "destination" -> Json.obj(
        "branch" -> Json.obj(
          "name" -> destinationBranch
        )
      )
    )

    client.postJson(Request(url, classOf[JsObject]), payload)
  }

  def postApprove(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/approve"
    client.postJson(Request(url, classOf[JsObject]), JsNull)
  }

  def deleteApprove(owner: String, repository: String, prId: Long): RequestResponse[Boolean] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/approve"
    client.delete(url)
  }

  def merge(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/merge"
    client.postJson(Request(url, classOf[JsObject]), JsNull)
  }

  def decline(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/decline"
    client.postJson(Request(url, classOf[JsObject]), JsNull)
  }

  private[this] def postNewComment(author: String, repo: String, prId: Int, values: JsObject): RequestResponse[PullRequestComment] = {
    val url = s"https://bitbucket.org/api/1.0/repositories/$author/$repo/pullrequests/$prId/comments"
    client.postJson(Request(url, classOf[PullRequestComment]), values)
  }

  def createPullRequestComment(author: String, repo: String, prId: Int, content: String): RequestResponse[PullRequestComment] = {
    val values = Json.obj("content" -> JsString(content))
    postNewComment(author, repo, prId, values)
  }

  def createLineComment(author: String, repo: String, prId: Int, commitUUID: String, body: String,
                        file: Option[String], line: Option[Int]): RequestResponse[PullRequestComment] = {
    val params = file.map(filename => "filename" -> JsString(filename)) ++
      line.map(lineTo => "line_to" -> JsNumber(lineTo))

    val values = JsObject(params.toSeq :+ "content" -> JsString(body) :+ "anchor" -> JsString(commitUUID.take(12)))
    postNewComment(author, repo, prId, values = values)
  }

  def deleteComment(author: String, repo: String, commitUUID: String, pullRequestId: Int, commentId: Long): Unit = {
    val url = s"https://bitbucket.org/api/1.0/repositories/$author/$repo/pullrequests/$pullRequestId/comments/$commentId"

    client.delete(url)
  }

  def listComments(author: String, repo: String, pullRequestId: Int): RequestResponse[Seq[SimplePullRequestComment]] = {
    val url = s"https://bitbucket.org/!api/1.0/repositories/$author/$repo/pullrequests/$pullRequestId/comments"

    client.execute(Request(url, classOf[Seq[SimplePullRequestComment]]))
  }

}
