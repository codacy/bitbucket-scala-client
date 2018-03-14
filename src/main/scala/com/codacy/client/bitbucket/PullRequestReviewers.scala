package com.codacy.client.bitbucket

import play.api.libs.json._

case class PullRequestReviewers(reviewers: Seq[String])

object PullRequestReviewers {

  implicit val reader: Reads[PullRequestReviewers] =
    (__ \ "reviewers" \ "uuid").read[PullRequestReviewers]

}
