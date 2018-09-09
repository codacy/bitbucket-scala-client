package com.codacy.client.bitbucket

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Email(email: String, is_primary: Boolean, is_confirmed: Boolean)

object Email {
  implicit def emailReader: Reads[Email] =
    ((__ \ "email").read[String] and
      (__ \ "is_primary").read[Boolean] and
      (__ \ "is_confirmed").read[Boolean]
      ) (Email.apply _)
}
