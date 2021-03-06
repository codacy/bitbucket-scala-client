package com.codacy.client.bitbucket.v1.service

import com.codacy.client.bitbucket.DefaultBodyWritables._
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v1.Issue
import play.api.libs.json.{JsString, Json}

class IssueServices(client: BitbucketClient) {

  def createIssue(author: String, repo: String, title: String, body: String): RequestResponse[Issue] = {

    val url = s"https://api.bitbucket.org/1.0/repositories/$author/$repo/issues"

    val values = Json.obj("title" -> JsString(title), "content" -> JsString(body))

    client.postForm(Request(url, classOf[Issue]), values)
  }

}
