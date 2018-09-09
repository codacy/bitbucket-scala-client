package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.User
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}

class TeamServices(client: BitbucketClient) {

  def list: RequestResponse[Seq[User]] = {
    client.executePaginated(Request(s"https://bitbucket.org/api/2.0/teams", classOf[Seq[User]]))
  }

}
