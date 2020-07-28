package com.codacy.client.bitbucket.client

import com.codacy.client.bitbucket.WSWrapper
import com.codacy.client.bitbucket.WSWrapper.WSClient
import com.codacy.client.bitbucket.client.Authentication.Credentials

class BitbucketClient(client: WSClient = WSWrapper.build(), credentials: Credentials)
    extends BitbucketClientBase(client, credentials)
