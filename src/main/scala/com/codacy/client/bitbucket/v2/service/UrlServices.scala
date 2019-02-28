package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.{JsNull, JsObject}
import com.codacy.client.bitbucket.client.Authentication.Credentials
import com.codacy.client.bitbucket.client.BitbucketAsyncClient
import play.api.libs.ws.ning.NingWSClient
import scala.concurrent.Future

class UrlServices(client: BitbucketClient) {

  /*
   * Post to a api url
   */
  def post(url: String): RequestResponse[JsObject] = {
    client.postJson(Request(url, classOf[JsObject]), JsNull)
  }

}

class AsyncUrlServices(client: BitbucketAsyncClient) {

  /*
   * Post to a api url
   */
  def post(url: String)(credentials: Credentials)(implicit nc: NingWSClient): Future[RequestResponse[JsObject]] = {
    client.postJson(Request(url, classOf[JsObject]), JsNull, credentials)
  }

}
