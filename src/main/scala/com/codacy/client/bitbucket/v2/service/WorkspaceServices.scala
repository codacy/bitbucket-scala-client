package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder

import com.codacy.client.bitbucket.client.{BitbucketClient, PageRequest, Request, RequestResponse}
import com.codacy.client.bitbucket.v2.{Workspace, WorkspacePermission}

class WorkspaceServices(client: BitbucketClient) {

  private val BaseUrl = "https://bitbucket.org/api/2.0/workspaces"

  def list(pageRequest: Option[PageRequest] = None): RequestResponse[Seq[Workspace]] = {

    pageRequest match {
      case Some(request) =>
        client.executeWithCursor[Workspace](BaseUrl, request)
      case None =>
        client.executePaginated(Request(BaseUrl, classOf[Seq[Workspace]]))
    }
  }

  /**
    * Gets the public information associated with a workspace.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    */
  def getWorkspace(username: String): RequestResponse[Workspace] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    client.execute(Request(s"$BaseUrl/$encodedUsername", classOf[Workspace]))
  }

  /**
    * Retrieve the permissions for all workspaces matching the supplied username.
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    * @param pageRequest The pagination request with cursor information
    * @return A [[RequestResponse]] with the user permissions for each workspace
    */
  def getWorkspacePermissions(
      username: String,
      pageRequest: Option[PageRequest] = None
  ): RequestResponse[Seq[WorkspacePermission]] = {
    val encodedUsername = URLEncoder.encode(username, "UTF-8")
    val baseRequestUrl = s"$BaseUrl/$encodedUsername/permissions"

    pageRequest match {
      case Some(request) =>
        client.executeWithCursor[WorkspacePermission](baseRequestUrl, request)
      case None =>
        client.executePaginated(Request(baseRequestUrl, classOf[Seq[WorkspacePermission]]))
    }
  }
}
