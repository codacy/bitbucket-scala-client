package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Team(
    username: String,
    nickname: String,
    account_status: String,
    display_name: String,
    website: String,
    created_on: String,
    uuid: String,
    has_2fa_enabled: Boolean,
    avatar: String
)

object Team {
  implicit val reader: Reads[Team] = (
    (__ \ "username").read[String] and
      (__ \ "nickname").read[String] and
      (__ \ "account_name").read[String] and
      (__ \ "display_name").read[String] and
      (__ \ "website").read[String] and
      (__ \ "created_on").read[String] and
      (__ \ "uuid").read[String] and
      (__ \ "has_2fa_enabled").read[Boolean] and
      (__ \ "links" \ "avatar" \ "href").read[String]
  )(Team.apply _)
}
