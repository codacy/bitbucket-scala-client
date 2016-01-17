package com.codacy.client.bitbucket

import play.api.libs.json._

case class BuildStatus(state: String, key: String, name: String, url: String, description: String)

object BuildStatus {
  implicit val fmt = Json.format[BuildStatus]
}
