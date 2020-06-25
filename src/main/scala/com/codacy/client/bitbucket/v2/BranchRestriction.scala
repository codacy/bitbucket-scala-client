package com.codacy.client.bitbucket.v2

import play.api.libs.json.{__, Reads}
import play.api.libs.functional.syntax._

case class BranchRestriction(kind: String, pattern: String, users: Seq[SimpleUser])

object BranchRestriction {
  implicit val reader: Reads[BranchRestriction] = (
    (__ \ "kind").read[String] and
      (__ \ "pattern").read[String] and
      (__ \ "users").read[Seq[SimpleUser]]
  )(BranchRestriction.apply _)
}
