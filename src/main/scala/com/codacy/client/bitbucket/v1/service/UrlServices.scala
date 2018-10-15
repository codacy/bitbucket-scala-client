package com.codacy.client.bitbucket.v1.service

import com.codacy.client.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.{JsNull, JsObject}

class UrlServices(client: BitbucketClient) {

  /*
   * Post to a api url
   */
  def post(url: String): RequestResponse[JsObject] = {
    client.postJson(Request(url, classOf[JsObject]), JsNull)
  }

}
