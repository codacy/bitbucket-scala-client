package com.codacy.client.bitbucket.v1.service

import com.codacy.client.bitbucket.v1.{SimpleRepository, SshKey}
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.Json
import com.codacy.client.bitbucket.client.Authentication.Credentials
import com.codacy.client.bitbucket.client.BitbucketAsyncClient
import play.api.libs.ws.ning.NingWSClient
import scala.concurrent.Future

class RepositoryServices(client: BitbucketClient) {

  /*
   * Gets the details of the repositories that the user owns or has at least read access to
   * Use this if you're looking for a full list of all of the repositories associated with a user
   */
  def getRepositories: RequestResponse[Seq[SimpleRepository]] = {
    client.execute(Request(s"https://bitbucket.org/api/1.0/user/repositories", classOf[Seq[SimpleRepository]]))
  }

  /*
   * Creates a ssh key
   */
  def createKey(username: String, repo: String, key: String, label: String = "Codacy Key"): RequestResponse[SshKey] = {
    val url = s"https://bitbucket.org/api/1.0/repositories/$username/$repo/deploy-keys"

    val values = Json.obj("key" -> key, "label" -> label)

    client.postJson(Request(url, classOf[SshKey]), values)
  }

}

class AsyncRepositoryServices(client: BitbucketAsyncClient) {

  /*
   * Gets the details of the repositories that the user owns or has at least read access to
   * Use this if you're looking for a full list of all of the repositories associated with a user
   */
  def getRepositories(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[Seq[SimpleRepository]]] = {
    client
      .execute(Request(s"https://bitbucket.org/api/1.0/user/repositories", classOf[Seq[SimpleRepository]]), credentials)
  }

  /*
   * Creates a ssh key
   */
  def createKey(username: String, repo: String, key: String, label: String = "Codacy Key")(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[SshKey]] = {
    val url = s"https://bitbucket.org/api/1.0/repositories/$username/$repo/deploy-keys"

    val values = Json.obj("key" -> key, "label" -> label)

    client.postJson(Request(url, classOf[SshKey]), values, credentials)
  }

}
