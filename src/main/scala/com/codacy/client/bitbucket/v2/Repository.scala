package com.codacy.client.bitbucket.v2

import java.time.LocalDateTime

import com.codacy.client.bitbucket.v2.Repository.Owner
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Repository(
    name: String,
    full_name: String,
    description: String,
    scm: String,
    created_on: LocalDateTime,
    updated_on: Option[LocalDateTime],
    owner: Owner,
    size: Option[Long],
    has_issues: Option[Boolean],
    is_private: Boolean,
    language: Option[String],
    urls: Seq[RepositoryUrl],
    uuid: String,
    slug: String
)

object Repository {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"
  val dateFormatWithoutMillis = "yyyy-MM-dd'T'HH:mm:ssXXX"

  implicit val dateTimeReads: Reads[LocalDateTime] =
    Reads
      .localDateTimeReads(dateFormat)
      .orElse(Reads.localDateTimeReads(dateFormatWithoutMillis))

  // format: off
  implicit val reader: Reads[Repository] = {
    ((__ \ "name").read[String] and
      (__ \ "full_name").read[String] and
      (__ \ "description").read[String] and 
      (__ \ "scm").read[String] and
      (__ \ "created_on").read[LocalDateTime] and
      (__ \ "updated_on").readNullable[LocalDateTime] and
      (__ \ "owner").read[Owner] and
      (__ \ "size").readNullable[Long] and
      (__ \ "has_issues").readNullable[Boolean] and
      (__ \ "is_private").read[Boolean] and
      (__ \ "language").readNullable[String] and
      (__ \ "links").read[Map[String, JsValue]].map(parseLinks) and
      (__ \ "uuid" ).read[String] and
      (__ \ "slug" ).read[String]
      ) (Repository.apply _)
  }
  // format: on

  final case class Owner(nickname: Option[String], username: Option[String], display_name: String, `type`: String)

  object Owner {
    implicit val reader: Reads[Owner] = Json.format[Owner]
  }

  private def parseLinks(links: Map[String, JsValue]): Seq[RepositoryUrl] = {
    links.flatMap {
      case (linkName, linkMap) =>
        val simpleLinks = for {
          ref <- linkMap.asOpt[Map[String, String]]
          urlType <- RepositoryUrlType.find(linkName)
          linkUrl <- ref.get("href")
        } yield RepositoryUrl(urlType, linkUrl)

        val complexLinks = for {
          refs <- linkMap.asOpt[Seq[Map[String, String]]].toSeq
          ref <- refs
          linkName <- ref.get("name")
          urlType <- RepositoryUrlType.find(linkName)
          linkUrl <- ref.get("href")
        } yield RepositoryUrl(urlType, linkUrl)

        simpleLinks ++ complexLinks
    }.toSeq
  }
}

object RepositoryUrlType extends Enumeration {
  val Https = Value("https")
  val Ssh = Value("ssh")

  def find(urlType: String): Option[Value] = {
    values.find(_.toString == urlType)
  }
}

case class RepositoryUrl(urlType: RepositoryUrlType.Value, link: String)

sealed trait OwnerInfo {
  def value: String
}

case class AccountUuid(value: String) extends OwnerInfo

case class TeamUsername(value: String) extends OwnerInfo
