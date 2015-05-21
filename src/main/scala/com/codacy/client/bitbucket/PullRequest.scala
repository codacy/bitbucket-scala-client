package com.codacy.client.bitbucket

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class PullRequest(id: Long, title: String, description: String,
                       authorUsername: String, authorAvatar: Option[String],
                       state: String, created_on: DateTime, updated_on: DateTime,
                       sourceRepository: String, sourceBranch: String, sourceCommit: String,
                       destRepository: String, destBranch: String, destCommit: String) {
  val url: String = s"https://bitbucket.org/$destRepository/pull-request/$id"
}

object PullRequest {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZZ"
  implicit val jodaDateTimeReads = Reads.jodaDateReads(dateFormat)

  implicit val reader: Reads[PullRequest] = (
    (__ \ "id").read[Long] and
      (__ \ "title").read[String] and
      (__ \ "description").read[String] and
      (__ \ "author" \ "username").read[String] and
      (__ \ "author" \ "links" \ "avatar").read[Option[String]] and
      (__ \ "state").read[String] and
      (__ \ "created_on").read[DateTime] and
      (__ \ "updated_on").read[DateTime] and
      (__ \ "source" \ "repository" \ "full_name").read[String] and
      (__ \ "source" \ "branch" \ "name").read[String] and
      (__ \ "source" \ "commit" \ "hash").read[String] and
      (__ \ "destination" \ "repository" \ "full_name").read[String] and
      (__ \ "destination" \ "branch" \ "name").read[String] and
      (__ \ "destination" \ "commit" \ "hash").read[String]
    )(PullRequest.apply _)
}
