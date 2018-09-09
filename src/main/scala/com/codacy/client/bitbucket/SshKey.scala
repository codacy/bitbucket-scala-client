package com.codacy.client.bitbucket

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class SshKey(uuid: String, key: String, label: String)

object SshKey {
  implicit val reader: Reads[SshKey] = (
    (__ \ "uuid").read[String] and
      (__ \ "key").read[String] and
      (__ \ "label").read[String]
    )(SshKey.apply _)
}
