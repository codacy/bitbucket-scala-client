package com.codacy.client.bitbucket.v2

import java.time.LocalDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class PullRequest(
    id: Long,
    title: String,
    description: String,
    author: User,
    state: String,
    created_on: LocalDateTime,
    updated_on: LocalDateTime,
    sourceRepository: String,
    sourceBranch: String,
    sourceCommit: String,
    destRepository: String,
    destBranch: String,
    destCommit: Option[String],
    apiUrls: Seq[ApiUrl]
) {
  val url = s"https://bitbucket.org/$destRepository/pull-request/$id"
}

object ApiUrlType extends Enumeration {
  val Commits = Value("commits")
  val Decline = Value("decline")
  val Self = Value("self")
  val Comments = Value("comments")
  val Patch = Value("patch")
  val Merge = Value("merge")
  val Html = Value("html")
  val Activity = Value("activity")
  val Diff = Value("diff")
  val Approve = Value("approve")

  def find(urlType: String): Option[Value] = {
    values.find(_.toString == urlType)
  }
}

case class ApiUrl(urlType: ApiUrlType.Value, link: String)

object PullRequest {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"
  implicit val dateTimeReads: Reads[LocalDateTime] = Reads.localDateTimeReads(dateFormat)

  implicit def optionStringReader: Reads[Option[String]] = Reads { (json: JsValue) =>
    json match {
      case JsString(value) => JsSuccess(Some(value))
      case _ => JsSuccess(None)
    }
  }

  // format: off
  implicit val reader: Reads[PullRequest] = (
    (__ \ "id").read[Long] and
      (__ \ "title").read[String] and
      (__ \ "description").read[String] and
      (__ \ "author").read[User] and
      (__ \ "state").read[String] and
      (__ \ "created_on").read[LocalDateTime] and
      (__ \ "updated_on").read[LocalDateTime] and
      (__ \ "source" \ "repository" \ "full_name").read[String] and
      (__ \ "source" \ "branch" \ "name").read[String] and
      (__ \ "source" \ "commit" \ "hash").read[String] and
      (__ \ "destination" \ "repository" \ "full_name").read[String] and
      (__ \ "destination" \ "branch" \ "name").read[String] and
      (__ \ "destination" \ "commit" \ "hash").readNullable[String] and
      // TODO: (__ \ "destination" \ "commit" \ "hash").read[Option[String]] and
      (__ \ "links").read[Map[String, Map[String, String]]].map(parseLinks)
    ) (PullRequest.apply _)
  // format: on

  private def parseLinks(links: Map[String, Map[String, String]]): Seq[ApiUrl] = {
    (for {
      (linkName, linkMap) <- links
      urlType <- ApiUrlType.find(linkName)
      linkUrl <- linkMap.get("href")
    } yield ApiUrl(urlType, linkUrl)).toSeq
  }
}
