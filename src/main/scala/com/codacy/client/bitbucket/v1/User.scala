package com.codacy.client.bitbucket.v1

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class User(username: String, first_name: String, last_name: String, is_team: Boolean)

object User {
  // format: off
  implicit val reader: Reads[User] = (
    (__ \ "user" \ "username").read[String] and
      (__ \ "user" \ "first_name").read[String] and
      (__ \ "user" \ "last_name").read[String] and
      (__ \ "user" \ "is_team").read[Boolean]
    )(User.apply _)
  // format: on
}
