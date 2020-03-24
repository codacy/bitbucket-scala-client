package com.codacy.client.bitbucket.client

import play.api.libs.ws.{WSAuthScheme, WSRequest}

/**
  * Handles request authentication.
  * Provides several different authentication options.
  *
  * @author - Robertas Zamblauskas
  */
object Authentication {

  sealed trait Credentials

  case class OAuth2Credentials(accessToken: String) extends Credentials

  /**
    * Your username and password | app password.
    */
  case class BasicAuthCredentials(username: String, password: String) extends Credentials

  sealed trait Authenticator {
    def authenticate(req: WSRequest): WSRequest
  }

  object Authenticator {

    def fromCredentials(credentials: Credentials): Authenticator = {
      credentials match {
        case c: OAuth2Credentials => new OAuth2Authenticator(c)
        case c: BasicAuthCredentials => new BasicAuthAuthenticator(c)
      }
    }
  }

  class OAuth2Authenticator(credentials: OAuth2Credentials) extends Authenticator {
    override def authenticate(req: WSRequest): WSRequest =
      req.withQueryString("access_token" -> credentials.accessToken)
  }

  class BasicAuthAuthenticator(credentials: BasicAuthCredentials) extends Authenticator {

    def authenticate(req: WSRequest): WSRequest =
      req.withAuth(credentials.username, credentials.password, WSAuthScheme.BASIC)
  }

  /**
    * Provide nicer syntax for authentication.
    */
  implicit class WsRequestExtensions(val req: WSRequest) extends AnyVal {
    def authenticate(authenticator: Authenticator): WSRequest = authenticator.authenticate(req)
  }
}
