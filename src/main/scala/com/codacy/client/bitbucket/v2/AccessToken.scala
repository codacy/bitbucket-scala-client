package com.codacy.client.bitbucket.v2

import play.api.libs.json.{Format, Json}

case class AccessToken(access_token: String, scopes: String, expires_in: Int, refresh_token: String, token_type: String)

object AccessToken {
  implicit val format: Format[AccessToken] = Json.format[AccessToken]
}
