package com.codacy.client.bitbucket.v1

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class SimplePullRequestComment(id: Long, anchor: Option[String])

object SimplePullRequestComment {

  implicit val reader: Reads[SimplePullRequestComment] = (
    (__ \ "comment_id").read[Long] and
      (__ \ "anchor").readNullable[String]
    )(SimplePullRequestComment.apply _)
}
