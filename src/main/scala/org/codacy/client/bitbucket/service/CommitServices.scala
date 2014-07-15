package org.codacy.client.bitbucket.service

import org.codacy.client.bitbucket.CommitComment
import org.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}

class CommitServices(client: BitbucketClient) {

  def createComment(author: String, repo: String, commit: String, body: String): RequestResponse[CommitComment] = {
    val url = s"https://bitbucket.org/!api/1.0/repositories/$author/$repo/changesets/${commit.take(12)}/comments"

    val values = Map(
      "content" -> Seq(body)
    )

    client.post(Request(url, classOf[CommitComment]), values)
  }

}
