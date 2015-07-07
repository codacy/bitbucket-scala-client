package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.{PullRequest, SimpleCommit}
import play.api.libs.json.{JsNull, JsObject, Json}

class PullRequestServices(client: BitbucketClient) {

  /*
   * Gets the list of a repository pull requests
   *
   * States: OPEN | MERGED | DECLINED
   *
   */
  def getPullRequests(owner: String, repository: String, states: Seq[String] = Seq("OPEN")): RequestResponse[Seq[PullRequest]] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests?state=${states.mkString(",")}"

    client.executePaginated(Request(url, classOf[Seq[PullRequest]]))
  }

  /*
   * Gets the list of commits of a pull request
   *
   */
  def getPullRequestCommits(owner: String, repository: String, prId: Long): RequestResponse[Seq[SimpleCommit]] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/commits"

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

    client.post(Request(url, classOf[JsObject]), payload)
  }

  def postApprove(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/approve"
    client.post(Request(url, classOf[JsObject]), JsNull)
  }

  def deleteApprove(owner: String, repository: String, prId: Long): RequestResponse[Boolean] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/approve"
    client.delete(url)
  }

  def merge(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/merge"
    client.post(Request(url, classOf[JsObject]), JsNull)
  }

  def decline(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/decline"
    client.post(Request(url, classOf[JsObject]), JsNull)
  }

}
