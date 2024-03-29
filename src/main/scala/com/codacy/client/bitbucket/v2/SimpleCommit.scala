package com.codacy.client.bitbucket.v2

import java.time.LocalDateTime

import play.api.libs.json._

@deprecated("Use BaseCommit instead", "18.7.1")
case class SimpleCommit(
    hash: String,
    authorName: Option[String],
    parents: Seq[String],
    date: LocalDateTime,
    message: String
)

object SimpleCommit {

  implicit val dateTimeReads: Reads[LocalDateTime] = Reads.localDateTimeReads("yyyy-MM-dd'T'HH:mm:ssXXX", identity)

  implicit def commitReader: Reads[SimpleCommit] = Reads { (json: JsValue) =>
    (for {
      hash <- (json \ "hash").asOpt[String]
      username = (json \ "author" \ "user" \ "username").asOpt[String]
      parents = (json \ "parents" \\ "hash").flatMap(_.asOpt[String])
      date <- (json \ "date").asOpt[LocalDateTime]
      message <- (json \ "message").asOpt[String]
    } yield SimpleCommit(hash, username, parents.toSeq /*cross-compile*/, date, message))
      .map(JsSuccess(_))
      .getOrElse(JsError("could not read commit"))
  }
}
