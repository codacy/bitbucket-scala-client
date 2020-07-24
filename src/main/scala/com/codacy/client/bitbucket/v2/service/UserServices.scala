package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v2._
import play.api.libs.json.Json

class UserServices(client: BitbucketClient) {

  /*
   * Gets the basic information associated with the token owner account.
   */
  def getUser: RequestResponse[User] = {
    client.execute(Request(client.userBaseUrl, classOf[User]))
  }

  /*
   * Gets all the emails of an account
   */
  def getEmails: RequestResponse[Seq[Email]] = {
    client.executePaginated(Request(s"${client.userBaseUrl}/emails", classOf[Seq[Email]]))
  }

  /*
   * Gets the basic information associated with an account.
   */
  def getUser(userId: String): RequestResponse[User] = {
    val encodedUserId = URLEncoder.encode(userId, "UTF-8")
    client.execute(Request(s"${client.usersBaseUrl}/$encodedUserId", classOf[User]))
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

    client.postJson(Request(url, classOf[SshKey]), values)
  }

}
