package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.v2.Issue
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json._
import com.codacy.client.bitbucket.client.Authentication.Credentials
import com.codacy.client.bitbucket.client.BitbucketAsyncClient
import play.api.libs.ws.ning.NingWSClient
import scala.concurrent.Future

class IssueServices(client: BitbucketClient) {

  def createIssue(author: String, repo: String, title: String, body: String): RequestResponse[Issue] = {

    val url = s"https://api.bitbucket.org/2.0/repositories/$author/$repo/issues"

    val values = Json.obj("title" -> JsString(title), "content" -> Json.obj("raw" -> JsString(body)))

    client.postForm(Request(url, classOf[Issue]), values)
  }

}

class AsyncIssueServices(client: BitbucketAsyncClient) {

  def createIssue(author: String, repo: String, title: String, body: String)(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[Issue]] = {

    val url = s"https://api.bitbucket.org/2.0/repositories/$author/$repo/issues"

    val values = Json.obj("title" -> JsString(title), "content" -> Json.obj("raw" -> JsString(body)))

    client.postForm(Request(url, classOf[Issue]), values, credentials)
  }

}
