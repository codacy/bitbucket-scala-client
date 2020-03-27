package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class SimpleTeam(uuid: String, displayName: String)

object SimpleTeam {
  implicit val reader: Reads[SimpleTeam] = {
    ((__ \ "uuid").read[String] and
      (__ \ "display_name").read[String])(SimpleTeam.apply _)
  }
}
