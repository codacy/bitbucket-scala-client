package com.codacy.client.bitbucket.client

import scala.util.Try

class PageRequest private (cursorValue: Option[String]) {
  val cursor = cursorValue
}

object PageRequest {

  def apply(
      cursor: Option[String],
      apiBaseUrl: String = BitbucketClientBase.apiBaseUrl
  ): Either[String, PageRequest] = {

    cursor match {
      case None => Right(new PageRequest(None))
      case Some(cursorValue) =>
        for {
          cursorURI <- validateURI(cursorValue, s"""Value "$cursorValue" is not a valid cursor: Malformed URL""")
          apiURI <- validateURI(apiBaseUrl, s"""Invalid api url $apiBaseUrl""")
          validationResult <- Either
            .cond(
              test = cursorURI.getHost == apiURI.getHost,
              right = new PageRequest(cursor),
              left = s"""Value "$cursorValue" is not a valid cursor: Hostname does not match Bitbucket Cloud API"""
            )
        } yield validationResult
    }
  }

  private def validateURI(uri: String, errorMessage: String): Either[String, java.net.URI] = {
    Try(java.net.URI.create(uri)).toOption.toRight[String](errorMessage)
  }
}
