package com.codacy.client.bitbucket.v2

import java.time.LocalDateTime

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Repository(name: String, full_name: String, description: String, scm: String,
                      created_on: LocalDateTime, updated_on: LocalDateTime, owner: String, size: Long,
                      has_issues: Boolean, is_private: Boolean, language: String,
                      urls: Seq[RepositoryUrl])

object Repository {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"
  val dateFormatWithoutMillis = "yyyy-MM-dd'T'HH:mm:ssXXX"

  implicit val dateTimeReads: Reads[LocalDateTime] =
    Reads.localDateTimeReads(dateFormat)
      .orElse(Reads.localDateTimeReads(dateFormatWithoutMillis))

  implicit val reader: Reads[Repository] = {
    ((__ \ "name").read[String] and
      (__ \ "full_name").read[String] and
      (__ \ "description").read[String] and
      (__ \ "scm").read[String] and
      (__ \ "created_on").read[LocalDateTime] and
      (__ \ "updated_on").read[LocalDateTime] and
      (__ \ "owner" \ "username").read[String] and
      (__ \ "size").read[Long] and
      (__ \ "has_issues").read[Boolean] and
      (__ \ "is_private").read[Boolean] and
      (__ \ "language").read[String] and
      (__ \ "links").read[Map[String, JsValue]].map(parseLinks)
      ) (Repository.apply _)
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
