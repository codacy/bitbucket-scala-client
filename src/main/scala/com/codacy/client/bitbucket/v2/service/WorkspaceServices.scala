package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder
import com.codacy.client.bitbucket.util.UrlHelper._
import com.codacy.client.bitbucket.client.{BitbucketClient, PageRequest, RequestResponse}
import com.codacy.client.bitbucket.v2.{Project, UserIdentifiers, UserIdentifiersApi, Workspace, WorkspacePermission}

class WorkspaceServices(client: BitbucketClient) {

  def list(pageRequest: Option[PageRequest] = None, pageLength: Option[Int] = None): RequestResponse[Seq[Workspace]] = {
    pageRequest match {
      case Some(request) =>
        client.executeWithCursor[Workspace](client.workspacesBaseUrl, request, pageLength)
      case None =>
        val length = pageLength.fold("")(pagelen => s"pagelen=$pagelen")
        val urlWithPageLength = joinQueryParameters(client.workspacesBaseUrl, length)
        client.executePaginated[Workspace](urlWithPageLength)
    }
  }

  def listAdmins(
      pageRequest: Option[PageRequest] = None,
      pageLength: Option[Int] = None,
      username: String
  ): RequestResponse[Seq[UserIdentifiers]] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    val permissionToFilter = URLEncoder.encode("""permission="owner"""", "UTF-8")
    val baseRequestUrl = s"""${client.workspacesBaseUrl}/$encodedUsername/members?q=$permissionToFilter"""
    val unfiltered = pageRequest match {
      case Some(request) =>
        client.executeWithCursor[UserIdentifiersApi](baseRequestUrl, request, pageLength)
      case None =>
        val length = pageLength.fold("")(pagelen => s"pagelen=$pagelen")
        val urlWithPageLength = joinQueryParameters(baseRequestUrl, length)
        client.executePaginated[UserIdentifiersApi](urlWithPageLength)
    }

    unfiltered.map { identifiersApis =>
      identifiersApis.collect {
        case UserIdentifiersApi(Some(accountId), uuid, "user") => UserIdentifiers(accountId, uuid)
      }
    }
  }

  /**
    * Gets the public information associated with a workspace.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    */
  def getWorkspace(username: String): RequestResponse[Workspace] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    client.execute[Workspace](s"${client.workspacesBaseUrl}/$encodedUsername")
  }

  /**
    * Retrieve the permissions for all workspaces matching the supplied username.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    * @param pageRequest The pagination request with cursor information.
    * @return A [[RequestResponse]] with the user permissions for each workspace
    */
  def getWorkspacePermissions(username: String, pageRequest: PageRequest): RequestResponse[Seq[WorkspacePermission]] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    val baseRequestUrl = s"${client.workspacesBaseUrl}/$encodedUsername/permissions"

    client.executeWithCursor[WorkspacePermission](baseRequestUrl, pageRequest)
  }

  /**
    * Retrieve the permissions for a user on a workspace.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    * @param userUuid The user UUID of the account surrounded by curly-braces
    * @return A [[RequestResponse]] with the user permissions for each workspace
    */
  def getWorkspacePermissionForUserByUuid(
      username: String,
      userUuid: String
  ): RequestResponse[Option[WorkspacePermission]] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    val userUuidEncoded = URLEncoder.encode(s""""$userUuid"""", "UTF-8")
    val baseRequestUrl = s"""${client.workspacesBaseUrl}/$encodedUsername/permissions?q=user.uuid=$userUuidEncoded"""

    val requestResponse = client.executePaginated[WorkspacePermission](baseRequestUrl)
    requestResponse.map(_.headOption)
  }

  /**
    * Retrieve the permissions for a user on a workspace.
    *
    * @param username The username or the UUID of the workspace surrounded by curly-braces
    * @param accountId The user account UUID of the account surrounded by curly-braces
    * @return A [[RequestResponse]] with the user permissions for each workspace
    */
  def getWorkspacePermissionForUserByAccountId(
      username: String,
      accountId: String
  ): RequestResponse[Option[WorkspacePermission]] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    val accountIdEncoded = URLEncoder.encode(s""""$accountId"""", "UTF-8")
    val baseRequestUrl = s"${client.workspacesBaseUrl}/$encodedUsername/permissions?q=user.account_id=$accountIdEncoded"
    val requestResponse = client.executePaginated[WorkspacePermission](baseRequestUrl)
    requestResponse.map(_.headOption)
  }

  def getWorkspaceProjects(
      workspace: String,
      pageRequest: Option[PageRequest] = None,
      pageLength: Option[Int] = None
  ): RequestResponse[Seq[Project]] = {
    val url = s"${client.workspacesBaseUrl}/$workspace/projects"

    pageRequest match {
      case Some(request) =>
        client.executeWithCursor[Project](url, request, pageLength)
      case None =>
        val length = pageLength.fold("")(pagelen => s"pagelen=$pagelen")
        val urlWithPageLength = joinQueryParameters(url, length)
        client.executePaginated[Project](urlWithPageLength)
    }
  }

}
