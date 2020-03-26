package com.codacy.client.bitbucket

import com.ning.http.client.AsyncHttpClientConfig
import play.api.libs.ws.ning.{NingAsyncHttpClientConfigBuilder, NingWSClient}

object WSWrapper {
  type WSClient = play.api.libs.ws.WSClient
  type WSRequest = play.api.libs.ws.WSRequest

  def withQueryString(request: WSRequest, parameters: (String, String)*): WSRequest = {
    request.withQueryString(parameters: _*)
  }

  def build(): WSClient = {
    val config = new NingAsyncHttpClientConfigBuilder().build()
    val clientConfig = new AsyncHttpClientConfig.Builder(config).build()
    new NingWSClient(clientConfig)
  }

}
