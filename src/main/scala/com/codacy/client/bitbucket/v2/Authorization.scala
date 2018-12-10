package com.codacy.client.bitbucket.v2

import play.api.libs.json.{Json, Writes}

object Authorization {

  sealed trait RefreshCredentials

  case class RefreshToken(refresh_token: String) extends RefreshCredentials {
    val grant_type = "refresh_token"
  }

  object RefreshToken {
    implicit val writer: Writes[RefreshToken] = Json.writes[RefreshToken]
  }

  object RefreshCredentials {
    implicit val writer: Writes[RefreshCredentials] =
      Writes[RefreshCredentials] {
        case c: RefreshToken => Json.toJson(c)(RefreshToken.writer)
      }
  }
}
