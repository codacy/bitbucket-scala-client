package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

import java.time.LocalDateTime

case class Account(
    uuid: Option[String],
    accountId: Option[String],
    username: Option[String],
    displayName: Option[String],
    nickname: Option[String],
    avatarLink: Option[String],
    createdOn: Option[LocalDateTime]
) {

  def getIdentifier: Option[String] =
    List(uuid, accountId).find(_.isDefined).flatten

  def getName: Option[String] =
    List(username, displayName, nickname).find(_.isDefined).flatten
}

object Account {

  implicit val accountReads: Reads[Account] = (
    (__ \ "uuid").readNullable[String] and
      (__ \ "account_id").readNullable[String] and
      (__ \ "username").readNullable[String] and
      (__ \ "display_name").readNullable[String] and
      (__ \ "nickname").readNullable[String] and
      (__ \ "links" \ "avatar" \ "href").readNullable[String] and
      (__ \ "created_on").readNullable[LocalDateTime]
  )(Account.apply _)

}
