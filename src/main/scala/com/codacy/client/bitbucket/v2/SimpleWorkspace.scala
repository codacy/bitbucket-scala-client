package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class SimpleWorkspace(uuid: String, displayName: String, avatar: String)

object SimpleWorkspace {
  implicit val reader: Reads[SimpleWorkspace] = {
    ((__ \ "uuid").read[String] and
      (__ \ "slug").read[String] and
      (__ \ "links" \ "avatar" \ "href").read[String])(SimpleWorkspace.apply _)
  }
}
