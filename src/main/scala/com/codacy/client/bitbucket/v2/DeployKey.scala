package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class DeployKey(id: Long, key: String, label: String)

object DeployKey {
  implicit val reader: Reads[DeployKey] = (
    (__ \ "id").read[Long] and
      (__ \ "key").read[String] and
      (__ \ "label").read[String]
    )(DeployKey.apply _)
}
