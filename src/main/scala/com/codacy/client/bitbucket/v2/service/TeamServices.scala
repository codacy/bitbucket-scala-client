package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.v2.Team
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}

class TeamServices(client: BitbucketClient) {

  def list(role: String = "member"): RequestResponse[Seq[Team]] = {
    client.executePaginated(Request(s"https://bitbucket.org/api/2.0/teams?role=$role", classOf[Seq[Team]]))
  }

}