package com.codacy.client.bitbucket.v2

import play.api.libs.json._

case class PullRequestReviewers(reviewers: Seq[String])

object PullRequestReviewers {

  // format: off
  implicit val reader: Reads[PullRequestReviewers] =
    (__ \ "reviewers" ).read(Reads.list((__ \ "uuid").read[String])).map(PullRequestReviewers.apply)
  // format: on

}
