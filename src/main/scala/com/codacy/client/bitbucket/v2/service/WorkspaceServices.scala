package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder
import com.codacy.client.bitbucket.util.UrlHelper._
import com.codacy.client.bitbucket.client.{BitbucketClient, PageRequest, Request, RequestResponse}
import com.codacy.client.bitbucket.v2.{User, UserIdentifiers, Workspace, WorkspacePermission}

class WorkspaceServices(client: BitbucketClient) {

  def list(pageRequest: Option[PageRequest] = None, pageLength: Option[Int] = None): RequestResponse[Seq[Workspace]] = {
    pageRequest match {
      case Some(request) =>
        client.executeWithCursor[Workspace](client.workspacesBaseUrl, request, pageLength)
      case None =>
        val length = pageLength.fold("")(pagelen => s"pagelen=$pagelen")
        val urlWithPageLength = joinQueryParameters(client.workspacesBaseUrl, length)
        client.executePaginated(Request(urlWithPageLength, classOf[Seq[Workspace]]))
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
    pageRequest match {
      case Some(request) =>
        client.executeWithCursor[UserIdentifiers](baseRequestUrl, request, pageLength)
      case None =>
        val length = pageLength.fold("")(pagelen => s"pagelen=$pagelen")
        val urlWithPageLength = joinQueryParameters(baseRequestUrl, length)
        client.executePaginated(Request(urlWithPageLength, classOf[Seq[UserIdentifiers]]))
    }
  }

  /**
    * Gets the public information associated with a workspace.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    */
  def getWorkspace(username: String): RequestResponse[Workspace] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    client.execute(Request(s"${client.workspacesBaseUrl}/$encodedUsername", classOf[Workspace]))
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

    val requestResponse = client.executePaginated(Request(baseRequestUrl, classOf[Seq[WorkspacePermission]]))
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
    val requestResponse = client.executePaginated(Request(baseRequestUrl, classOf[Seq[WorkspacePermission]]))
    requestResponse.map(_.headOption)
  }
}
