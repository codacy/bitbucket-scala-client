package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.client.DefaultBodyWritables._
import com.codacy.client.bitbucket.client.{BitbucketClient, RequestResponse}
import com.codacy.client.bitbucket.v2.AccessToken
import com.codacy.client.bitbucket.v2.Authorization.{RefreshCredentials, RefreshToken}

class AuthorizationServices(client: BitbucketClient) {

  private val authorizationUrl = "https://bitbucket.org/site/oauth2/access_token"

  /*
   * Gets new AccessToken with the RefreshCredentials
   *
   */
  def refreshAccessToken(credentials: RefreshCredentials): RequestResponse[AccessToken] = {
    val values = credentials match {
      case c: RefreshToken => Map("grant_type" -> Seq(c.grant_type), "refresh_token" -> Seq(c.refresh_token))
    }

    client.postForm[Map[String, Seq[String]], AccessToken](authorizationUrl, values)
  }
}
