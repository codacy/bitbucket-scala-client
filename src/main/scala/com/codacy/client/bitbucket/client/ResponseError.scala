package com.codacy.client.bitbucket.client

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class ResponseError(id: String, detail: String, message: String)

case class ThrowableResponseError(id: String, detail: String, message: String) extends Exception

object ResponseError {
  // format: off
  implicit val reader: Reads[ResponseError] = (
    (__ \ "id").read[String] and
      (__ \ "details").read[String] and
      (__ \ "message").read[String]
    )(ResponseError.apply _)
  // format: on
}
