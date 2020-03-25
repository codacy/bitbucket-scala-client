package com.codacy.client.bitbucket

object WSWrapper {
  type WSClient = play.api.libs.ws.WSClient
  type WSRequest = play.api.libs.ws.WSRequest

  def withQueryString(request: WSRequest, parameters: (String, String)*): WSRequest = {
    request.withQueryString(parameters: _*)
  }

}
