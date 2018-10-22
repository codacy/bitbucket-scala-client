package com.codacy.client.bitbucket.v1

import java.time.LocalDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class SimpleRepository(name: String, description: String, scm: String, created_on: LocalDateTime, updated_on: LocalDateTime,
                            owner: String, size: Long, has_issues: Boolean, is_private: Boolean, language: String) {

  val full_name: String = s"$owner/$name"
  val sshUrl: String = s"ssh://$scm@bitbucket.org/$owner/$name"
  val httpsUrl: String = s"https://$owner@bitbucket.org/$owner/$name"
}

object SimpleRepository {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS"
  val dateFormatWithoutMillis = "yyyy-MM-dd'T'HH:mm:ss"

  implicit val dateTimeReads: Reads[LocalDateTime] =
    Reads.localDateTimeReads(dateFormat)
      .orElse(Reads.localDateTimeReads(dateFormatWithoutMillis))

  implicit val reader: Reads[SimpleRepository] =
    ((__ \ "slug").read[String] and
      (__ \ "description").read[String] and
      (__ \ "scm").read[String] and
      (__ \ "created_on").read[LocalDateTime] and
      (__ \ "last_updated").read[LocalDateTime] and
      (__ \ "owner").read[String] and
      (__ \ "size").read[Long] and
      (__ \ "has_issues").read[Boolean] and
      (__ \ "is_private").read[Boolean] and
      (__ \ "language").read[String]
      ) (SimpleRepository.apply _)
}
