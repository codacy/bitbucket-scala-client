package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class SimpleRepository(uuid: String, name: String, fullName: String)

object SimpleRepository {
  implicit val reader: Reads[SimpleRepository] = {
    ((__ \ "uuid").read[String] and
      (__ \ "name").read[String] and
      (__ \ "full_name").read[String])(SimpleRepository.apply _)
  }
}
