package org.codacy.client.bitbucket

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Issue(id: Long, status: String, priority: String, title: String, content: String, owner: String,
                 created_on: DateTime, kind: String)

object Issue {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
  implicit val jodaDateTimeReads = Reads.jodaDateReads(dateFormat)

  implicit val reader: Reads[Issue] = (
    (__ \ "local_id").read[Long] and
      (__ \ "status").read[String] and
      (__ \ "priority").read[String] and
      (__ \ "title").read[String] and
      (__ \ "content").read[String] and
      (__ \ "reported_by" \ "username").read[String] and
      (__ \ "created_on").read[DateTime] and
      (__ \ "metadata" \ "kind").read[String]
    )(Issue.apply _)
}