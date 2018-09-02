package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.Issue
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json._
import play.api.libs.ws.JsonBodyWritables._

class IssueServices(client: BitbucketClient) {

  def createIssue(author: String, repo: String, title: String, body: String): RequestResponse[Issue] = {

    val url = s"https://api.bitbucket.org/2.0/repositories/$author/$repo/issues"

    val values = Json.obj("title" -> JsString(title), "content" -> Json.obj("raw" -> JsString(body)))

    client.postForm(Request(url, classOf[Issue]), values)
  }

}
