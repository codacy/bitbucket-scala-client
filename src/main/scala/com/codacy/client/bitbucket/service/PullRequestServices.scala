package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.{Commit, PullRequest}
import play.api.libs.json.JsObject

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
  def getPullRequestCommits(owner: String, repository: String, prId: Long): RequestResponse[Seq[Commit]] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/commits"

    client.executePaginated(Request(url, classOf[Seq[Commit]]))
  }

  def postApprove(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/approve"
    client.post(Request(url, classOf[JsObject]), Map.empty)
  }

  def deleteApprove(owner: String, repository: String, prId: Long): RequestResponse[Boolean] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/approve"
    client.delete(url)
  }

  def merge(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/merge"
    client.post(Request(url, classOf[JsObject]), Map.empty)
  }

  def decline(owner: String, repository: String, prId: Long): RequestResponse[JsObject] = {
    val url = s"https://bitbucket.org/!api/2.0/repositories/$owner/$repository/pullrequests/$prId/decline"
    client.post(Request(url, classOf[JsObject]), Map.empty)
  }

}
