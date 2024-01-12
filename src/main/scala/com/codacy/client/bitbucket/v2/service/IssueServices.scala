package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder
import com.codacy.client.bitbucket.client.DefaultBodyWritables._
import com.codacy.client.bitbucket.client.{BitbucketClient, RequestResponse}
import com.codacy.client.bitbucket.v2.Issue
import play.api.libs.json._

class IssueServices(client: BitbucketClient) {

  def createIssue(author: String, repo: String, title: String, body: String): RequestResponse[Issue] = {
    val encodedAuthor = URLEncoder.encode(author, "UTF-8")
    val encodedRepo = URLEncoder.encode(repo, "UTF-8")
    val url = s"${client.repositoriesBaseUrl}/$encodedAuthor/$encodedRepo/issues"

    val values = Json.obj("title" -> JsString(title), "content" -> Json.obj("raw" -> JsString(body)))

    client.postForm[JsObject, Issue](url, values)
  }

}
