package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class UserIdentifiers(account_id: String, uuid: Option[String])

object UserIdentifiers {
  implicit val reader: Reads[UserIdentifiers] =
    ((__ \ "user" \ "account_id").read[String] and
      (__ \ "user" \ "uuid").readNullable[String])(UserIdentifiers.apply _)
}
