package com.codacy.client.bitbucket

import play.api.libs.json.{Json, Reads}

case class FieldValue(name:String,value:String)
case class ServiceValue(fields:Seq[FieldValue],`type`:String)
case class Service(id:Long, service:ServiceValue)

object Service{
  implicit val reads: Reads[Service] = {
    implicit lazy val r0 = Json.reads[FieldValue]
    implicit lazy val r1 = Json.reads[ServiceValue]
    Json.reads[Service]
  }
}