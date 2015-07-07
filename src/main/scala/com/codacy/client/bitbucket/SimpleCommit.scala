package com.codacy.client.bitbucket

import org.joda.time.DateTime
import play.api.libs.json._

case class SimpleCommit(hash: String, authorName: Option[String], parents: Seq[String], date: DateTime, message: String)

object SimpleCommit {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ssZZ"
  implicit val jodaDateTimeReads = Reads.jodaDateReads(dateFormat)

  implicit def commitReader: Reads[SimpleCommit] = Reads { (json: JsValue) =>
    (for {
      hash <- (json \ "hash").asOpt[String]
      username = (json \ "author" \ "user" \ "username").asOpt[String]
      parents = (json \ "parents" \\ "hash").flatMap(_.asOpt[String])
      date <- (json \ "date").asOpt[DateTime]
      message <- (json \ "message").asOpt[String]
    } yield SimpleCommit(hash, username, parents, date, message))
      .map(JsSuccess(_))
      .getOrElse(JsError("could not read commit"))
  }
}
