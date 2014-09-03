package com.codacy.client.bitbucket

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Repository(name: String, full_name: String, description: String,
                      scm: String, created_on: DateTime, updated_on: DateTime,
                      httpsUrl: String, sshUrl: String, owner: String, size: Long,
                      has_issues: Boolean, is_private: Boolean, language: String)

object Repository {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZZ"
  implicit val jodaDateTimeReads = Reads.jodaDateReads(dateFormat)

  implicit val reader: Reads[Repository] = {
    val url1 = (__ \ "links" \ "clone")(0).read[Url]
    val url2 = (__ \ "links" \ "clone")(1).read[Url]

    val httpUrl = url1.filter(_.name == "https").orElse(url2).map(_.href)
    val sshUrl = url2.filter(_.name == "ssh").orElse(url1).map(_.href)

    ((__ \ "name").read[String] and
      (__ \ "full_name").read[String] and
      (__ \ "description").read[String] and
      (__ \ "scm").read[String] and
      (__ \ "created_on").read[DateTime] and
      (__ \ "updated_on").read[DateTime] and
      httpUrl and
      sshUrl and
      (__ \ "owner" \ "username").read[String] and
      (__ \ "size").read[Long] and
      (__ \ "has_issues").read[Boolean] and
      (__ \ "is_private").read[Boolean] and
      (__ \ "language").read[String]
      )(Repository.apply _)
  }
}

case class Url(name: String, href: String)

object Url {
  implicit val reader: Reads[Url] = (
    (__ \ "name").read[String] and
      (__ \ "href").read[String]
    )(Url.apply _)
}
