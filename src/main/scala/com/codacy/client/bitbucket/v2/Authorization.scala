package com.codacy.client.bitbucket.v2

object Authorization {

  sealed trait RefreshCredentials {
    val grant_type: String
  }

  case class RefreshToken(refresh_token: String) extends RefreshCredentials {
    val grant_type: String = "refresh_token"
  }
}
