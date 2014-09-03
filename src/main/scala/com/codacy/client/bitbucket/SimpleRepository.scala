package com.codacy.client.bitbucket

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class SimpleRepository(name: String, description: String, scm: String, created_on: DateTime, updated_on: DateTime,
                            owner: String, size: Long, has_issues: Boolean, is_private: Boolean, language: String) {

  val full_name: String = s"$owner/$name"
  val sshUrl: String = s"ssh://$scm@bitbucket.org/$owner/$name"
  val httpsUrl: String = s"https://$owner@bitbucket.org/$owner/$name"
}

object SimpleRepository {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
  implicit val jodaDateTimeReads = Reads.jodaDateReads(dateFormat)

  implicit val reader: Reads[SimpleRepository] =
    ((__ \ "slug").read[String] and
      (__ \ "description").read[String] and
      (__ \ "scm").read[String] and
      (__ \ "created_on").read[DateTime] and
      (__ \ "last_updated").read[DateTime] and
      (__ \ "owner").read[String] and
      (__ \ "size").read[Long] and
      (__ \ "has_issues").read[Boolean] and
      (__ \ "is_private").read[Boolean] and
      (__ \ "language").read[String]
      )(SimpleRepository.apply _)
}
