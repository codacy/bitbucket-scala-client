package com.codacy.client.bitbucket

import java.time.LocalDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Issue(id: Long, state: String, priority: String, title: String, content: String, reporter: String,
                 created_on: LocalDateTime, kind: String)

object Issue {
  implicit val reader: Reads[Issue] = (
    (__ \ "id").read[Long] and
      (__ \ "state").read[String] and
      (__ \ "priority").read[String] and
      (__ \ "title").read[String] and
      (__ \ "content" \ "raw").read[String] and
      (__ \ "reporter" \ "username").read[String] and
      (__ \ "created_on").read[LocalDateTime] and
      (__ \ "kind").read[String]
    )(Issue.apply _)
}
