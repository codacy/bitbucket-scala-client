package com.codacy.client.bitbucket.v1

import java.time.LocalDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class PullRequestComment(id: Long, username: String, display_name: String, content: String, created_on: LocalDateTime)

object PullRequestComment {
  val dateFormat = "yyyy-MM-dd HH:mm:ssXXX"
  implicit val dateTimeReads: Reads[LocalDateTime] = Reads.localDateTimeReads(dateFormat)

  implicit val reader: Reads[PullRequestComment] = (
    (__ \ "comment_id").read[Long] and
      (__ \ "username").read[String] and
      (__ \ "display_name").read[String] and
      (__ \ "content").read[String] and
      (__ \ "utc_created_on").read[LocalDateTime]
    )(PullRequestComment.apply _)
}
