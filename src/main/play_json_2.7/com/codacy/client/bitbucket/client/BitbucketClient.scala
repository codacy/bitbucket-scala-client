package com.codacy.client.bitbucket.client

import akka.stream.Materializer
import com.codacy.client.bitbucket.WSWrapper
import com.codacy.client.bitbucket.WSWrapper.WSClient
import com.codacy.client.bitbucket.client.Authentication.Credentials

class BitbucketClient(credentials: Credentials, materializer: Materializer) extends BitbucketClientBase(credentials) {
  override protected def buildClient(): WSClient = WSWrapper.build(materializer)
}
