package com.codacy.client.bitbucket.v1.service

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v1.{Email, SshKey, User}
import play.api.libs.json.Json

class UserServices(client: BitbucketClient) {

  /*
   * Gets the basic information associated with the token owner account.
   */
  def getUser: RequestResponse[User] = {
    client.execute(Request("https://bitbucket.org/api/1.0/user", classOf[User]))
  }

  /*
   * Gets the basic information associated with an account.
   */
  def getUser(username: String): RequestResponse[User] = {
    client.execute(Request(s"https://bitbucket.org/api/1.0/users/$username", classOf[User]))
  }

  /*
   * Gets all the emails of an account
   */
  def getEmails(username: String): RequestResponse[Seq[Email]] = {
    client.execute(Request(s"https://bitbucket.org/api/1.0/users/$username/emails", classOf[Seq[Email]]))
  }

  /*
   * Creates a ssh key
   */
  def createKey(username: String, key: String, keyName: String = "Codacy Key"): RequestResponse[SshKey] = {
    val url = s"https://bitbucket.org/api/1.0/users/$username/ssh-keys"

    val values = Json.obj(
      "key" -> key,
      "label" -> keyName
    )

    client.postJson(Request(url, classOf[SshKey]), values)
  }

}
