package com.codacy.client.bitbucket

object WSWrapper {
  type WSClient = play.api.libs.ws.StandaloneWSClient
  type WSRequest = play.api.libs.ws.StandaloneWSRequest

  def withHttpHeaders(request: WSRequest, headers: (String, String)*): WSRequest = {
    request.withHttpHeaders(headers: _*)
  }

  def build(materializer: akka.stream.Materializer): WSClient = {
    play.api.libs.ws.ahc.StandaloneAhcWSClient()(materializer)
  }

}
