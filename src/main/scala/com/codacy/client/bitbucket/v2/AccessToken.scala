package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json.{__, Reads}

case class AccessToken(access_token: String, refresh_token: String, expires_in: Int, scope: String, token_type: String)

object AccessToken {

  // format: off
  implicit val reader: Reads[AccessToken] = (
    (__ \ "access_token").read[String] and
      (__ \ "refresh_token").read[String] and
      (__ \ "expires_in").read[Int] and
      (__ \ "scope").read[String].orElse((__ \ "scopes").read[String]) and
      (__ \ "token_type").read[String]
  )(AccessToken.apply _)
  // format: on
}
