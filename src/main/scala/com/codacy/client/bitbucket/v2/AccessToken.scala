package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json.{Reads, __}

case class AccessToken(access_token: String, refresh_token: String)

object AccessToken {
  implicit val reader: Reads[AccessToken] = (
    (__ \ "access_token").read[String] and
      (__ \ "refresh_token").read[String]
  )(AccessToken.apply _)
}