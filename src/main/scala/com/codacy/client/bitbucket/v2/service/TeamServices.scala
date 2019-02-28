package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.v2.Team
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.client.Authentication.Credentials
import com.codacy.client.bitbucket.client.BitbucketAsyncClient
import play.api.libs.ws.ning.NingWSClient
import scala.concurrent.Future

class TeamServices(client: BitbucketClient) {

  def list(role: String = "member"): RequestResponse[Seq[Team]] = {
    client.executePaginated(Request(s"https://bitbucket.org/api/2.0/teams?role=$role", classOf[Seq[Team]]))
  }

}

class AsyncTeamServices(client: BitbucketAsyncClient) {

  def list(
      role: String = "member"
  )(credentials: Credentials)(implicit nc: NingWSClient): Future[RequestResponse[Seq[Team]]] = {
    client.executePaginated(Request(s"https://bitbucket.org/api/2.0/teams?role=$role", classOf[Seq[Team]]), credentials)
  }

}
