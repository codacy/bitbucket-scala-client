package com.codacy.client.bitbucket.v2

import play.api.libs.json._

case class SimpleCommitSha(value: String)

object SimpleCommitSha {
  implicit def commitReader: Reads[SimpleCommitSha] = Reads { (json: JsValue) =>
    (for {
      hash <- (json \ "hash").asOpt[String]
    } yield SimpleCommitSha(hash))
      .map(JsSuccess(_))
      .getOrElse(JsError("could not read commit"))
  }
}
