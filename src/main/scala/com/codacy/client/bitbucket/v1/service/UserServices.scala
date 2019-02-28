package com.codacy.client.bitbucket.v1.service

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v1.{Email, SshKey, User}
import play.api.libs.json.Json
import com.codacy.client.bitbucket.client.Authentication.Credentials
import com.codacy.client.bitbucket.client.BitbucketAsyncClient
import play.api.libs.ws.ning.NingWSClient
import scala.concurrent.Future

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

    val values = Json.obj("key" -> key, "label" -> keyName)

    client.postJson(Request(url, classOf[SshKey]), values)
  }

}

class AsyncUserServices(client: BitbucketAsyncClient) {

  /*
   * Gets the basic information associated with the token owner account.
   */
  def getUser(credentials: Credentials)(implicit nc: NingWSClient): Future[RequestResponse[User]] = {
    client.execute(Request("https://bitbucket.org/api/1.0/user", classOf[User]), credentials)
  }

  /*
   * Gets the basic information associated with an account.
   */
  def getUser(username: String)(credentials: Credentials)(implicit nc: NingWSClient): Future[RequestResponse[User]] = {
    client.execute(Request(s"https://bitbucket.org/api/1.0/users/$username", classOf[User]), credentials)
  }

  /*
   * Gets all the emails of an account
   */
  def getEmails(
      username: String
  )(credentials: Credentials)(implicit nc: NingWSClient): Future[RequestResponse[Seq[Email]]] = {
    client.execute(Request(s"https://bitbucket.org/api/1.0/users/$username/emails", classOf[Seq[Email]]), credentials)
  }

  /*
   * Creates a ssh key
   */
  def createKey(username: String, key: String, keyName: String = "Codacy Key")(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[SshKey]] = {
    val url = s"https://bitbucket.org/api/1.0/users/$username/ssh-keys"

    val values = Json.obj("key" -> key, "label" -> keyName)

    client.postJson(Request(url, classOf[SshKey]), values, credentials)
  }

}
