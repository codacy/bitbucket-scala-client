package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.v2.{Team, UserPermission}
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}

class TeamServices(client: BitbucketClient) {

  private val BaseUrl = "https://bitbucket.org/api/2.0/teams"

  def list(role: String = "member"): RequestResponse[Seq[Team]] = {
    client.executePaginated(Request(s"$BaseUrl?role=$role", classOf[Seq[Team]]))
  }

  /**
    * Retrieve the permissions matching the supplied username and repositorySlug.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces.
    * @param repositorySlug The repository slug or the UUID of the repository surrounded by curly-braces.
    * @return A [[RequestResponse]] with the user permissions for the repository
    */
  def getRepositoryUserPermissions(username: String, repositorySlug: String): RequestResponse[Seq[UserPermission]] = {
    //TODO: do proper pagination after CY-1210
    client.executePaginated(
      Request(s"$BaseUrl/$username/permissions/repositories/$repositorySlug", classOf[Seq[UserPermission]])
    )
  }

}
