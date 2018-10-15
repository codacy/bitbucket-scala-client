package com.codacy.client.client

import play.api.libs.oauth.{ConsumerKey, OAuthCalculator, RequestToken}
import play.api.libs.ws.{WSAuthScheme, StandaloneWSRequest => WSRequest}

/**
  * Handles request authentication.
  * Provides several different authentication options.
  *
  * @author - Robertas Zamblauskas
  */
object Authentication {

  sealed trait Credentials

  case class OAuthCredentials(key: String, secretKey: String, token: String, secretToken: String) extends Credentials

  case class OAuth2Credentials(accessToken: String) extends Credentials

  /**
    * Your username and password | app password.
    */
  case class BasicAuthCredentials(username: String, password: String) extends Credentials


  sealed trait Authenticator {
    def authenticate(req: WSRequest): WSRequest
  }

  object Authenticator {
    def fromCredentials(credentials: Credentials): Authenticator =  {
      credentials match {
        case c: OAuthCredentials     => new OAuthAuthenticator(c)
        case c: OAuth2Credentials    => new OAuth2Authenticator(c)
        case c: BasicAuthCredentials => new BasicAuthAuthenticator(c)
      }
    }
  }

  class OAuthAuthenticator(credentials: OAuthCredentials) extends Authenticator {
    private lazy val KEY = ConsumerKey(credentials.key, credentials.secretKey)
    private lazy val TOKEN = RequestToken(credentials.token, credentials.secretToken)

    private lazy val requestSigner = OAuthCalculator(KEY, TOKEN)

    def authenticate(req: WSRequest): WSRequest = req.sign(requestSigner)
  }

  class OAuth2Authenticator(credentials: OAuth2Credentials) extends Authenticator {
    override def authenticate(req: WSRequest): WSRequest = req.withQueryStringParameters("access_token" -> credentials.accessToken)
  }

  class BasicAuthAuthenticator(credentials: BasicAuthCredentials) extends Authenticator {
    def authenticate(req: WSRequest): WSRequest = req.withAuth(credentials.username, credentials.password, WSAuthScheme.BASIC)
  }

  /**
    * Provide nicer syntax for authentication.
    */
  implicit class WsRequestExtensions(val req: WSRequest) extends AnyVal {
    def authenticate(authenticator: Authenticator): WSRequest = authenticator.authenticate(req)
  }
}
