package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.CommitComment
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.Json

class CommitServices(client: BitbucketClient) {

  def createComment(author: String, repo: String, commit: String, body: String): RequestResponse[CommitComment] = {
    val url = s"https://bitbucket.org/!api/1.0/repositories/$author/$repo/changesets/${commit.take(12)}/comments"

    val values = Json.obj("content" -> body)

    client.post(Request(url, classOf[CommitComment]), values)
  }

}
