package com.codacy.client.bitbucket

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Commit(hash: String, authorName: String, parents: Option[Seq[String]], date: DateTime, message: String)

object Commit {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ssZZ"
  implicit val jodaDateTimeReads = Reads.jodaDateReads(dateFormat)

  implicit val reader: Reads[Commit] = (
    (__ \ "hash").read[String] and
      (__ \ "author" \ "user" \ "username").read[String] and
      (__ \ "parents" \\ "hash").read[Option[Seq[String]]] and
      (__ \ "date").read[DateTime] and
      (__ \ "message").read[String]
    )(Commit.apply _)
}