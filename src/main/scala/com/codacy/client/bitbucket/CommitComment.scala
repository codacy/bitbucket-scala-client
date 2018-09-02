package com.codacy.client.bitbucket

import java.time.LocalDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class CommitComment(id: Long, commit: String, content: String, created_on: LocalDateTime, deleted: Boolean)

object CommitComment {
  implicit val reader: Reads[CommitComment] = (
    (__ \ "id").read[Long] and
      (__ \ "commit" \ "hash").read[String] and
      (__ \ "content" \ "raw").read[String] and
      (__ \ "created_on").read[LocalDateTime] and
      (__ \ "deleted").read[Boolean]
    )(CommitComment.apply _)
}
