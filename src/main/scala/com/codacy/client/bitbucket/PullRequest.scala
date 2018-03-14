package com.codacy.client.bitbucket

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class PullRequest(id: Long, title: String, description: String,
                       authorUsername: Option[String], authorAvatar: Option[String],
                       authorId: Option[String], state: String, created_on: DateTime, updated_on: DateTime,
                       sourceRepository: String, sourceBranch: String, sourceCommit: String,
                       destRepository: String, destBranch: String, destCommit: Option[String],
                       apiUrls: Seq[ApiUrl]) {
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
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZZ"
  implicit val jodaDateTimeReads = Reads.jodaDateReads(dateFormat)

  implicit def optionStringReader: Reads[Option[String]] = Reads { (json: JsValue) =>
    json match {
      case JsString(value) => JsSuccess(Some(value))
      case _ => JsSuccess(None)
    }
  }

  implicit val reader: Reads[PullRequest] = (
    (__ \ "id").read[Long] and
      (__ \ "title").read[String] and
      (__ \ "description").read[String] and
      (__ \ "author" \ "username").readNullable[String] and
      (__ \ "author" \ "links" \ "avatar" \ "href").readNullable[String].orElse((__ \ "author" \ "links").readNullable[String]) and
      (__ \ "author" \ "uuid").readNullable[String] and
      (__ \ "state").read[String] and
      (__ \ "created_on").read[DateTime] and
      (__ \ "updated_on").read[DateTime] and
      (__ \ "source" \ "repository" \ "full_name").read[String] and
      (__ \ "source" \ "branch" \ "name").read[String] and
      (__ \ "source" \ "commit" \ "hash").read[String] and
      (__ \ "destination" \ "repository" \ "full_name").read[String] and
      (__ \ "destination" \ "branch" \ "name").read[String] and
      (__ \ "destination" \ "commit" \ "hash").readNullable[String] and
      // TODO: (__ \ "destination" \ "commit" \ "hash").read[Option[String]] and
      (__ \ "links").read[Map[String, Map[String, String]]].map(parseLinks)
    ) (PullRequest.apply _)

  private def parseLinks(links: Map[String, Map[String, String]]): Seq[ApiUrl] = {
    (for {
      (linkName, linkMap) <- links
      urlType <- ApiUrlType.find(linkName)
      linkUrl <- linkMap.get("href")
    } yield ApiUrl(urlType, linkUrl)).toSeq
  }
}
