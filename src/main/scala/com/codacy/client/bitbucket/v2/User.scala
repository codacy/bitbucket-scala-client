package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class User(username: String, display_name: String)

object User {
  // format: off
  implicit val reader: Reads[User] = (
    (__ \ "username").read[String] and
      (__ \ "display_name").read[String]
    )(User.apply _)
  // format: on
}
