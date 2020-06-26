package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

final case class WorkspacePermission(user: SimpleUser, team: SimpleWorkspace, permission: String)

object WorkspacePermission {
  implicit val reader: Reads[WorkspacePermission] = {
    ((__ \ "user").read[SimpleUser] and
      (__ \ "workspace").read[SimpleWorkspace] and
      (__ \ "permission").read[String])(WorkspacePermission.apply _)
  }
}
