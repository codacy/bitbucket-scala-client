package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

final case class TeamPermission(user: SimpleUser, team: SimpleTeam, permission: String)

object TeamPermission {
  implicit val reader: Reads[TeamPermission] = {
    ((__ \ "user").read[SimpleUser] and
      (__ \ "team").read[SimpleTeam] and
      (__ \ "permission").read[String])(TeamPermission.apply _)
  }
}
