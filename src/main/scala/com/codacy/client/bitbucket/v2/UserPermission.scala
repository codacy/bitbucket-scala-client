package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

final case class UserPermission(user: SimpleUser, repository: SimpleRepository, permission: String)

object UserPermission {
  implicit val reader: Reads[UserPermission] = {
    ((__ \ "user").read[SimpleUser] and
      (__ \ "repository").read[SimpleRepository] and
      (__ \ "permission").read[String])(UserPermission.apply _)
  }
}
