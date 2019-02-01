package com.codacy.client.bitbucket.v1

import java.time.LocalDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class CommitComment(
    id: Long,
    username: String,
    commit: String,
    display_name: String,
    content: String,
    created_on: LocalDateTime
)

object CommitComment {
  val dateFormat = "yyyy-MM-dd HH:mm:ssXXX"
  implicit val dateTimeReads: Reads[LocalDateTime] = Reads.localDateTimeReads(dateFormat)

  // format: off
  implicit val reader: Reads[CommitComment] = (
    (__ \ "comment_id").read[Long] and
      (__ \ "username").read[String] and
      (__ \ "node").read[String] and
      (__ \ "display_name").read[String] and
      (__ \ "content").read[String] and
      (__ \ "utc_created_on").read[LocalDateTime]
    )(CommitComment.apply _)
  // format: on
}
