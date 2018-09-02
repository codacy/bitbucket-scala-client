package com.codacy.client.bitbucket

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class User(username: String, display_name: String)

object User {
  implicit val reader: Reads[User] = (
    (__ \ "username").read[String] and
      (__ \ "display_name").read[String]
    )(User.apply _)
}
