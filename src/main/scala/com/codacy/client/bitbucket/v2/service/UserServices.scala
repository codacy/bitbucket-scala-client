package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.v2.{Email, SshKey, Team, User}
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.Json

class UserServices(client: BitbucketClient) {

  /*
   * Gets the basic information associated with the token owner account.
   */
  def getUser: RequestResponse[User] = {
    client.execute(Request("https://api.bitbucket.org/2.0/user", classOf[User]))
  }

  /*
   * Gets the basic information associated with an account.
   */
  def getUser(username: String): RequestResponse[User] = {
    client.execute(Request(s"https://api.bitbucket.org/2.0/users/$username", classOf[User]))
  }

  /*
   * Gets all the emails of an account
   */
  def getEmails: RequestResponse[Seq[Email]] = {
    client.executePaginated(Request(s"https://bitbucket.org/api/2.0/user/emails", classOf[Seq[Email]]))
  }

  /*
   * Gets all the teams a user is a member of
   */
  def getTeams: RequestResponse[Seq[Team]] = {
    client.executePaginated(Request(s"https://bitbucket.org/api/2.0/user/permissions/teams", classOf[Seq[Team]]))
  }

  /*
   * Creates a ssh key
   */
  def createKey(username: String, key: String, keyName: String): RequestResponse[SshKey] = {
    val url = s"https://bitbucket.org/api/2.0/users/$username/ssh-keys"

    val values = Json.obj(
      "key" -> key,
      "label" -> keyName
    )

    client.postJson(Request(url, classOf[SshKey]), values)
  }

}