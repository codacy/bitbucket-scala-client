package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.PullRequest
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}

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

}
