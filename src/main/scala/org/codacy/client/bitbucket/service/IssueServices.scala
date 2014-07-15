package org.codacy.client.bitbucket.service

import org.codacy.client.bitbucket.Issue
import org.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}

class IssueServices(client: BitbucketClient) {

  def createIssue(author: String, repo: String, title: String, body: String): RequestResponse[Issue] = {
    val url = s"https://bitbucket.org/!api/1.0/repositories/$author/$repo/issues"

    val values = Map(
      "title" -> Seq(title),
      "content" -> Seq(body)
    )

    client.post(Request(url, classOf[Issue]), values)
  }

}
