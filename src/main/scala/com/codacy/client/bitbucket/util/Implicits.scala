package com.codacy.client.bitbucket.util

import play.api.data.validation.ValidationError
import play.api.libs.json.{Json, Reads, Writes}
import scala.language.implicitConversions

object Implicits {

  implicit def enumWrites[E <: Enumeration#Value]: Writes[E] = Writes((e: E) => Json.toJson(e.toString))

  implicit def enumReads[E <: Enumeration](e: E): Reads[e.Value] = {
    Reads.StringReads.map { case value => e.values.find(_.toString == value) }.
      collect(ValidationError("Invalid enumeration value")) { case Some(v) => v }
  }
}