package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Team(username: String, display_name: String)

object Team {
  implicit val reader: Reads[Team] = (
    (__ \ "team" \ "username").read[String] and
      (__ \ "team" \ "display_name").read[String]
    )(Team.apply _)
}