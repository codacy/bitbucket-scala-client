package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Workspace(uuid: String, slug: String, name: String, avatar: String)

object Workspace {
  implicit val reader: Reads[Workspace] = (
    (__ \ "uuid").read[String] and
      (__ \ "slug").read[String] and
      (__ \ "name").read[String] and
      (__ \ "links" \ "avatar" \ "href").read[String]
  )(Workspace.apply _)
}
