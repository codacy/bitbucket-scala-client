package com.codacy.client.bitbucket.v1.service

import com.codacy.client.bitbucket.v1.Issue
import com.codacy.client.client.{BitbucketClient, Request, RequestResponse}

class IssueServices(client: BitbucketClient) {

  def createIssue(author: String, repo: String, title: String, body: String): RequestResponse[Issue] = {

    val url = s"https://api.bitbucket.org/1.0/repositories/$author/$repo/issues"

    val values = Map("title" -> Seq(title), "content" -> Seq(body))

    client.postForm(Request(url, classOf[Issue]), values)
  }

}
