package com.codacy.client.bitbucket.v2

import play.api.libs.json.{JsObject, Json, Reads}

case class Webhook(
    uuid: String,
    description: Option[String],
    url: String,
    subject: JsObject,
    events: Set[String],
    active: Boolean,
    created_at: String,
    links: JsObject
)

object Webhook {
  implicit val reads: Reads[Webhook] = Json.reads[Webhook].map {
    case hook =>
      hook.uuid match {
        case uuid if uuid.startsWith("{") && uuid.endsWith("}") =>
          hook.copy(uuid = uuid.drop(1).dropRight(1))
        case _ => hook
      }
  }
}
