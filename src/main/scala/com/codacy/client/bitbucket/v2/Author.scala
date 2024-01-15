package com.codacy.client.bitbucket.v2

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Author(raw: String, user: Option[Account])

object Author {

  implicit val authorReads: Reads[Author] = (
    (__ \ "raw").read[String] and
      (__ \ "user").readNullable[Account]
  )(Author.apply _)
}
