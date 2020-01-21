package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class User(
    account_id: String,
    uuid: Option[String],
    display_name: String,
    nickname: Option[String],
    avatarUrl: Option[String]
)

object User {
  // format: off
  implicit val reader: Reads[User] = (
    (__ \ "account_id").read[String] and
      (__ \ "uuid").readNullable[String] and
      (__ \ "display_name").read[String] and
      (__ \ "nickname").readNullable[String] and
      (__ \ "links" \ "avatar" \ "href").readNullable[String]
    )(User.apply _)
  // format: on
}
