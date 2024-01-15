package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

import java.time.LocalDateTime

case class Account(
    uuid: String,
    username: String,
    displayName: Option[String],
    avatarLink: Option[String],
    createdOn: LocalDateTime
)

object Account {

  implicit val accountReads: Reads[Account] = (
    (__ \ "uuid").read[String] and
      (__ \ "username").read[String] and
      (__ \ "display_name").readNullable[String] and
      (__ \ "links" \ "avatar" \ "href").readNullable[String] and
      (__ \ "created_on").read[LocalDateTime]
  )(Account.apply _)

}
