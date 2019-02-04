package com.codacy.client.bitbucket.v2

import java.time.LocalDateTime

import play.api.libs.json._

case class SimpleCommit(
    hash: String,
    authorName: Option[String],
    parents: Seq[String],
    date: LocalDateTime,
    message: String
)

object SimpleCommit {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX"
  implicit val dateTimeReads: Reads[LocalDateTime] = Reads.localDateTimeReads(dateFormat, s => { println(s); s })

  implicit def commitReader: Reads[SimpleCommit] = Reads { (json: JsValue) =>
    (for {
      hash <- (json \ "hash").asOpt[String]
      username = (json \ "author" \ "user" \ "username").asOpt[String]
      parents = (json \ "parents" \\ "hash").flatMap(_.asOpt[String])
      date <- (json \ "date").asOpt[LocalDateTime]
      message <- (json \ "message").asOpt[String]
    } yield SimpleCommit(hash, username, parents, date, message))
      .map(JsSuccess(_))
      .getOrElse(JsError("could not read commit"))
  }
}
