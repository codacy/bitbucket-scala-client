package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v2.AccessToken
import com.codacy.client.bitbucket.v2.Authorization.RefreshCredentials
import play.api.libs.json._

class AuthorizationServices(client: BitbucketClient) {

  /*
   * Gets new AccessToken with the RefreshCredentials
   *
   */
  def refreshAccessToken(
      credentials: RefreshCredentials): RequestResponse[AccessToken] = {
    val url = s"https://bitbucket.org/site/oauth2/access_token"

    val values = Json.toJson[RefreshCredentials](credentials)

    client.postJson(Request(url, classOf[AccessToken]), values)
  }
}
