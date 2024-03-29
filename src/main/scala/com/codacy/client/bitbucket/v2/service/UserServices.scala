package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder
import com.codacy.client.bitbucket.client.{
  BitbucketClient,
  FailedResponse,
  PageRequest,
  RequestResponse,
  SuccessfulResponse
}
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
    * Search for workspaceUUID among the workspaces that the current user is member of
    * returns the workspace and their effective role
    */
  def getWorkspaceMembership(workspaceUUID: String): RequestResponse[WorkspacePermission] = {
    val workspaceUUIDEncoded = URLEncoder.encode(s""""$workspaceUUID"""", "UTF-8")
    val workspaceQuery = s"""workspace.uuid=$workspaceUUIDEncoded"""

    client.executeWithCursor[WorkspacePermission](
      s"""${client.userBaseUrl}/permissions/workspaces?q=$workspaceQuery""",
      PageRequest()
    ) match {
      case SuccessfulResponse(membership +: _, _, _, _, _, _) =>
        if (membership.team.uuid == workspaceUUID) {
          SuccessfulResponse(membership)
        } else {
          FailedResponse(s"UUIDs don't match. Queried $workspaceUUID but received ${membership.team.uuid}")
        }
      case error: FailedResponse => error
      case _ => FailedResponse(s"Couldn't find $workspaceUUID for the current user")
    }
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
