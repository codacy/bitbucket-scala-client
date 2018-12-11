package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v2.AccessToken
import com.codacy.client.bitbucket.v2.Authorization.{RefreshCredentials, RefreshToken}

class AuthorizationServices(client: BitbucketClient) {

  /*
   * Gets new AccessToken with the RefreshCredentials
   *
   */
  def refreshAccessToken(
      credentials: RefreshCredentials): RequestResponse[AccessToken] = {
    val url = "https://bitbucket.org/site/oauth2/access_token"

    val values = credentials match {
      case c: RefreshToken => Map("grant_type" -> Seq(c.grant_type), "refresh_token" -> Seq(c.refresh_token))
    }

    client.postForm(Request(url, classOf[AccessToken]), values)
  }
}
