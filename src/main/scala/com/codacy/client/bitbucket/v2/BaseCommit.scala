package com.codacy.client.bitbucket.v2

import play.api.libs.json._

import java.time.LocalDateTime

case class BaseCommit(hash: String, parents: Seq[String], date: LocalDateTime, message: String, author: Author)

object BaseCommit {

  implicit val dateTimeReads: Reads[LocalDateTime] = Reads.localDateTimeReads("yyyy-MM-dd'T'HH:mm:ssXXX", identity)

  implicit def commitReader: Reads[BaseCommit] = Reads { (json: JsValue) =>
    (for {
      hash <- (json \ "hash").asOpt[String]
      parents = (json \ "parents" \\ "hash").flatMap(_.asOpt[String])
      date <- (json \ "date").asOpt[LocalDateTime]
      message <- (json \ "message").asOpt[String]
      author <- (json \ "author").asOpt[Author]
    } yield BaseCommit(hash, parents.toSeq /*cross-compile*/, date, message, author))
      .map(JsSuccess(_))
      .getOrElse(JsError("could not read commit"))
  }
}
