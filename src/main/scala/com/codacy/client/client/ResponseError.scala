package com.codacy.client.client

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class ResponseError(id: String, detail: String, message: String)

object ResponseError {
  implicit val reader: Reads[ResponseError] = (
    (__ \ "id").read[String] and
      (__ \ "details").read[String] and
      (__ \ "message").read[String]
    )(ResponseError.apply _)
}
