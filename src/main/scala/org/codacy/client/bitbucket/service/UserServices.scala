package org.codacy.client.bitbucket.service

import org.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import org.codacy.client.bitbucket.{SshKey, User}

class UserServices(client: BitbucketClient) {

  /*
   * Gets the basic information associated with the token owner account.
   */
  def getUser: RequestResponse[User] = {
    client.execute(Request("https://bitbucket.org/!api/1.0/user", classOf[User]))
  }

  /*
   * Gets the basic information associated with an account.
   */
  def getUser(username: String): RequestResponse[User] = {
    client.execute(Request(s"https://bitbucket.org/!api/1.0/users/$username", classOf[User]))
  }

  /*
   * Creates a ssh key
   */
  def createKey[T](username: String, key: String): RequestResponse[SshKey] = {
    val url = s"https://bitbucket.org/!api/1.0/users/$username/ssh-keys"

    val values = Map(
      "key" -> Seq(key),
      "label" -> Seq("Codacy Key")
    )

    client.post(Request(url, classOf[SshKey]), values)
  }

}
