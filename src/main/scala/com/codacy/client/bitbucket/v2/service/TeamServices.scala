package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder

import com.codacy.client.bitbucket.client.{BitbucketClient, PageRequest, Request, RequestResponse}
import com.codacy.client.bitbucket.v2.{Team, TeamPermission, UserPermission}

class TeamServices(client: BitbucketClient) {

  private val BaseUrl = "https://bitbucket.org/api/2.0/teams"

  def list(role: String = "member", pageRequest: Option[PageRequest] = None): RequestResponse[Seq[Team]] = {
    val baseRequestUrl = s"$BaseUrl?role=$role"

    pageRequest match {
      case Some(request) =>
        client.executeWithCursor[Team](baseRequestUrl, request)
      case None =>
        client.executePaginated(Request(baseRequestUrl, classOf[Seq[Team]]))
    }
  }

  /**
    * Gets the public information associated with a team.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    */
  def getTeam(username: String): RequestResponse[Team] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    client.execute(Request(s"$BaseUrl/$encodedUsername", classOf[Team]))
  }

  /**
    * Retrieve the permissions for all teams matching the supplied username.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    * @param pageRequest The pagination request with cursor information
    * @return A [[RequestResponse]] with the user permissions for each team
    */
  def getTeamUserPermissions(
      username: String,
      pageRequest: Option[PageRequest] = None
  ): RequestResponse[Seq[TeamPermission]] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    val baseRequestUrl = s"$BaseUrl/$encodedUsername/permissions"

    pageRequest match {
      case Some(request) =>
        client.executeWithCursor[TeamPermission](baseRequestUrl, request)
      case None =>
        client.executePaginated(Request(baseRequestUrl, classOf[Seq[TeamPermission]]))
    }
  }

  /**
    * Retrieve the permissions matching the supplied username and repositorySlug.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    * @param repositorySlug The repository slug or the UUID of the repository surrounded by curly-braces
    * @param pageRequest The pagination request with cursor information
    * @return A [[RequestResponse]] with the user permissions for the repository
    */
  def getTeamRepositoryUserPermissions(
      username: String,
      repositorySlug: String,
      pageRequest: Option[PageRequest] = None
  ): RequestResponse[Seq[UserPermission]] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    val encodedRepositorySlug = URLEncoder.encode(repositorySlug, "UTF-8")
    val baseRequestUrl = s"$BaseUrl/$encodedUsername/permissions/repositories/$encodedRepositorySlug"

    pageRequest match {
      case Some(request) =>
        client.executeWithCursor[UserPermission](baseRequestUrl, request)
      case None =>
        client.executePaginated(Request(baseRequestUrl, classOf[Seq[UserPermission]]))
    }
  }
}
