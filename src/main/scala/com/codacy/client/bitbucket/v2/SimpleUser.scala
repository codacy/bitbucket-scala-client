package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class SimpleUser(uuid: String, displayName: String)

object SimpleUser {
  implicit val reader: Reads[SimpleUser] = (
    (__ \ "uuid").read[String] and
      (__ \ "display_name").read[String]
  )(SimpleUser.apply _)
}
