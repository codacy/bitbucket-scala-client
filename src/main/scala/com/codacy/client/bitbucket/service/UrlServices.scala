package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.JsObject

class UrlServices(client: BitbucketClient) {

  /*
   * Post to a api url
   */
  def post(url: String): RequestResponse[JsObject] = {
    client.post(Request(url, classOf[JsObject]), Map.empty)
  }

}
