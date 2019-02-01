package com.codacy.client.bitbucket.v1

import java.time.LocalDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Commit(hash: String, authorName: String, parents: Option[Seq[String]], date: LocalDateTime, message: String)

object Commit {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX"
  implicit val dateTimeReads: Reads[LocalDateTime] = Reads.localDateTimeReads(dateFormat)

  implicit def optionSeqStringReader: Reads[Option[Seq[String]]] = Reads { (json: JsValue) =>
    json match {
      case JsArray(value) => JsSuccess(Some(value.flatMap(_.asOpt[String])))
      case _ => JsSuccess(None)
    }
  }

  // format: off
  implicit val reader: Reads[Commit] = (
    (__ \ "hash").read[String] and
      (__ \ "author" \ "user" \ "username").read[String] and
      (__ \ "parents" \\ "hash").read[Option[Seq[String]]] and
      (__ \ "date").read[LocalDateTime] and
      (__ \ "message").read[String]
    )(Commit.apply _)
  // format: on
}
