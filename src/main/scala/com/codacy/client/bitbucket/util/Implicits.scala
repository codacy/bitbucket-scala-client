package com.codacy.client.bitbucket.util

import java.net.URI

import play.api.data.validation.ValidationError
import play.api.libs.json.{Json, Reads, Writes}

import scala.language.implicitConversions

object Implicits {

  implicit def enumWrites[E <: Enumeration#Value]: Writes[E] = Writes((e: E) => Json.toJson(e.toString))

  implicit def enumReads[E <: Enumeration](e: E): Reads[e.Value] = {
    Reads.StringReads.map { value => e.values.find(_.toString == value) }.
      collect(ValidationError("Invalid enumeration value")) { case Some(v) => v }
  }

  implicit class URIQueryParam(uri: URI) {
    def addQuery(q: String): URI = {
      val newQuery = if (Option.apply(uri.getQuery).isEmpty) {
        q
      } else {
        uri.getQuery + "&" + q
      }

      new URI(uri.getScheme, uri.getAuthority, uri.getPath, newQuery, uri.getFragment)
    }
  }

}
