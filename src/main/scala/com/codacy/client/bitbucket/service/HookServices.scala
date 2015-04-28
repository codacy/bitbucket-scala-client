package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.Service
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}

class HookServices(client: BitbucketClient) {

  def createPostPushHook(author: String, repo: String, hookUrl:String): RequestResponse[Service] = {
    val url = s"https://bitbucket.org/!api/1.0/repositories/$author/$repo/services"

    val payload = Map(
      "type" -> "POST",
      "URL"  -> hookUrl
    ).mapValues(Seq(_))

    client.post(Request(url, classOf[Service]), payload)
  }
}
