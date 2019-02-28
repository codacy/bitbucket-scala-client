package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.v2.{DeployKey, OwnerInfo, Repository}
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.Json
import com.codacy.client.bitbucket.client.Authentication.Credentials
import com.codacy.client.bitbucket.client.BitbucketAsyncClient
import play.api.libs.ws.ning.NingWSClient
import scala.concurrent.Future

class RepositoryServices(client: BitbucketClient) {

  /*
   * Gets the list of the user's repositories. Private repositories only appear on this list
   * if the caller is authenticated and is authorized to view the repository.
   */
  def getRepositories(ownerInfo: OwnerInfo): RequestResponse[Seq[Repository]] = {
    client.executePaginated(
      Request(s"https://bitbucket.org/api/2.0/repositories/${ownerInfo.value}", classOf[Seq[Repository]])
    )
  }

  def createKey(
      username: String,
      repo: String,
      key: String,
      label: String = "Codacy Key"
  ): RequestResponse[DeployKey] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$username/$repo/deploy-keys"

    val values = Json.obj("key" -> key, "label" -> label)

    client.postJson(Request(url, classOf[DeployKey]), values)
  }
}

class AsyncRepositoryServices(client: BitbucketAsyncClient) {

  /*
   * Gets the list of the user's repositories. Private repositories only appear on this list
   * if the caller is authenticated and is authorized to view the repository.
   */
  def getRepositories(
      ownerInfo: OwnerInfo
  )(credentials: Credentials)(implicit nc: NingWSClient): Future[RequestResponse[Seq[Repository]]] = {
    client.executePaginated(
      Request(s"https://bitbucket.org/api/2.0/repositories/${ownerInfo.value}", classOf[Seq[Repository]]),
      credentials
    )
  }

  def createKey(username: String, repo: String, key: String, label: String = "Codacy Key")(
      credentials: Credentials
  )(implicit nc: NingWSClient): Future[RequestResponse[DeployKey]] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$username/$repo/deploy-keys"

    val values = Json.obj("key" -> key, "label" -> label)

    client.postJson(Request(url, classOf[DeployKey]), values, credentials)
  }
}
