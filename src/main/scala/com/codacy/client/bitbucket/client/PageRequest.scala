package com.codacy.client.bitbucket.client

import scala.util.Try

class PageRequest private (cursorValue: Option[String]) {
  val cursor = cursorValue
}

object PageRequest {

  def apply(cursor: Option[String], apiBaseUrl: String): Either[String, PageRequest] = {
    cursor match {
      case None => Right(new PageRequest(None))
      case Some(cursorValue) =>
        for {
          cursorURI <- Try(java.net.URI.create(cursorValue)).toOption
            .toRight(s"""Value "$cursorValue" is not a valid cursor: Malformed URL""")
          apiURI <- Try(java.net.URI.create(apiBaseUrl)).toOption
            .toRight(s"""Invalid api url $apiBaseUrl""")
          validationResult <- Either
            .cond(
              test = cursorURI.getHost == apiURI.getHost,
              right = new PageRequest(cursor),
              left = s"""Value "$cursorValue" is not a valid cursor: Hostname does not match Bitbucket Server API"""
            )
        } yield validationResult
    }
  }
}
