package com.codacy.client.bitbucket

import play.api.libs.json.{Reads, Json, JsObject}

case class Webhook(uuid:String,description:String,url:String,subject:JsObject,events:Set[String],active:Boolean,created_at:String,links:JsObject)
object Webhook{
  implicit val reads: Reads[Webhook] = Json.reads[Webhook].map{ case hook =>
    hook.uuid match{
      case uuid if uuid.startsWith("{") && uuid.endsWith("}") =>
        hook.copy(uuid = uuid.drop(1).dropRight(1))
      case _ => hook
    }
  }
}