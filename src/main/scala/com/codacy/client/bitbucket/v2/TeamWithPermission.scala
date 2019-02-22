package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class TeamWithPermission(team: Team, permission: String)

object TeamWithPermission {
  // format: off
  implicit val reader: Reads[TeamWithPermission] = (
    (__ \ "team").read[Team] and
      (__ \ "permission").read[String]
    )(TeamWithPermission.apply _)
  // format: on
}
