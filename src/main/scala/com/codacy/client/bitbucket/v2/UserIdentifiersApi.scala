package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

private[v2] case class UserIdentifiersApi(account_id: Option[String], uuid: Option[String], `type`: String)

object UserIdentifiersApi {
  implicit val reader: Reads[UserIdentifiersApi] =
    ((__ \ "user" \ "account_id").readNullable[String] and
      (__ \ "user" \ "uuid").readNullable[String] and
      (__ \ "user" \ "type").read[String])(UserIdentifiersApi.apply _)
}
