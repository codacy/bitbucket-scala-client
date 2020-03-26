package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Team(uuid: String, username: String, display_name: String, avatar: String)

object Team {
  implicit val reader: Reads[Team] = (
    (__ \ "uuid").read[String] and
      (__ \ "username").read[String] and
      (__ \ "display_name").read[String] and
      (__ \ "links" \ "avatar" \ "href").read[String]
  )(Team.apply _)
}
