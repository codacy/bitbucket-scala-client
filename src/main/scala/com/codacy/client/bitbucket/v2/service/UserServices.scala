package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder
import com.codacy.client.bitbucket.client.{BitbucketClient, RequestResponse}
import com.codacy.client.bitbucket.v2._
import play.api.libs.json.Json

class UserServices(client: BitbucketClient) {

  /**
    * Gets the basic information associated with the token owner account.
    */
  def getUser: RequestResponse[User] =
    client.execute[User](client.userBaseUrl)

  /**
    * Gets all the emails of an account
    */
  def getEmails: RequestResponse[Seq[Email]] =
    client.executePaginated[Email](s"${client.userBaseUrl}/emails")

  /**
    * Get the current user's permission on a specific workspace.
    * Uses the new /user/workspaces/{workspace}/permission endpoint
    * (replaces deprecated /user/permissions/workspaces).
    */
  def getWorkspaceMembership(workspaceUUID: String): RequestResponse[WorkspacePermission] = {
    val workspaceUUIDEncoded = URLEncoder.encode(workspaceUUID, "UTF-8")
    client.execute[WorkspacePermission](s"${client.userBaseUrl}/workspaces/$workspaceUUIDEncoded/permission")
  }

  /**
    * Gets the basic information associated with an account.
    */
  def getUser(userId: String): RequestResponse[User] = {
    val encodedUserId = URLEncoder.encode(userId, "UTF-8")
    client.execute[User](s"${client.usersBaseUrl}/$encodedUserId")
  }

  /**
    * Creates an SSH key for the specified user
    *
    * @param owner The username or the UUID of the account surrounded by curly-braces
    * @param key Public key to add on the user account
    * @param keyName Name of the created key
    */
  def createKey(owner: OwnerInfo, key: String, keyName: String): RequestResponse[SshKey] = {
    val encodedOwner = URLEncoder.encode(owner.value, "UTF-8")
    val url = s"${client.usersBaseUrl}/$encodedOwner/ssh-keys"

    val values = Json.obj("key" -> key, "label" -> keyName)

    client.postJson[SshKey](url, values)
  }

}
