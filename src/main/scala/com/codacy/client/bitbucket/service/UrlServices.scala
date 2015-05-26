package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.JsValue

class UrlServices(client: BitbucketClient) {

  /*
   * Post to a api url
   */
  def post(url: String): RequestResponse[JsValue] = {
    client.post(Request(url, classOf[JsValue]), Map.empty)
  }

}
