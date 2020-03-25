package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.DefaultBodyWritables._
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v2.Issue
import play.api.libs.json.{JsString, Json}

class IssueServices(client: BitbucketClient) {

  def createIssue(author: String, repo: String, title: String, body: String): RequestResponse[Issue] = {

    val url = s"https://api.bitbucket.org/2.0/repositories/$author/$repo/issues"

    val values = Json.obj("title" -> JsString(title), "content" -> Json.obj("raw" -> JsString(body)))

    client.postForm(Request(url, classOf[Issue]), values)
  }

}
