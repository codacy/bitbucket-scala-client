package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Project(name: String, key: String)

object Project {
  implicit val reads: Reads[Project] = (
    (__ \ "name").read[String] and
      (__ \ "key").read[String]
  )(Project.apply _)
}
