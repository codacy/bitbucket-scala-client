package com.codacy.client.bitbucket.client

import akka.stream.Materializer
import com.codacy.client.bitbucket.WSWrapper
import com.codacy.client.bitbucket.WSWrapper.WSClient
import com.codacy.client.bitbucket.client.Authentication.Credentials

class BitbucketClient(materializer: Materializer)(
    client: WSClient = WSWrapper.build(materializer),
    credentials: Credentials
) extends BitbucketClientBase(client, credentials)
