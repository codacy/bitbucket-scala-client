package com.codacy.client.bitbucket

import java.time.LocalDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class PullRequestComment(id: Long, content: String, created_on: LocalDateTime, deleted: Boolean)

object PullRequestComment {
  implicit val reader: Reads[PullRequestComment] = (
    (__ \ "id").read[Long] and
      (__ \ "content" \ "raw").read[String] and
      (__ \ "created_on").read[LocalDateTime] and
      (__ \ "deleted").read[Boolean]
    )(PullRequestComment.apply _)
}
