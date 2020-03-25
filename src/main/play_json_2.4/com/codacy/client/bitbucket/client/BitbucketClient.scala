package com.codacy.client.bitbucket.client

import com.codacy.client.bitbucket.WSWrapper
import com.codacy.client.bitbucket.WSWrapper.WSClient
import com.codacy.client.bitbucket.client.Authentication.Credentials

import scala.util.Try

class BitbucketClient(credentials: Credentials) extends BitbucketClientBase(credentials) {
  override protected def buildClient(): WSClient = WSWrapper.build()
}
