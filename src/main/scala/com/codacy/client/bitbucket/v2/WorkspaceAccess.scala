package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * Represents a user's access to a workspace from the /user/workspaces endpoint.
  *
  * @param administrator Whether the user has administrator access to the workspace
  * @param workspace The workspace details
  */
case class WorkspaceAccess(administrator: Boolean, workspace: Workspace)

object WorkspaceAccess {
  implicit val reader: Reads[WorkspaceAccess] = (
    (__ \ "administrator").read[Boolean] and
      (__ \ "workspace").read[Workspace]
  )(WorkspaceAccess.apply _)
}
