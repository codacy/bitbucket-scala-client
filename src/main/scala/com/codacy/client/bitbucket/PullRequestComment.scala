package com.codacy.client.bitbucket

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class PullRequestComment(id: Long, username: String, display_name: String, content: String, created_on: DateTime)

object PullRequestComment {
  val dateFormat = "yyyy-MM-dd HH:mm:ssZZ"
  implicit val jodaDateTimeReads = Reads.jodaDateReads(dateFormat)

  implicit val reader: Reads[PullRequestComment] = (
    (__ \ "comment_id").read[Long] and
      (__ \ "username").read[String] and
      (__ \ "display_name").read[String] and
      (__ \ "content").read[String] and
      (__ \ "utc_created_on").read[DateTime]
    )(PullRequestComment.apply _)
}