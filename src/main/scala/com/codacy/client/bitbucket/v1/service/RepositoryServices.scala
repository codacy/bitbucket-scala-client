package com.codacy.client.bitbucket.v1.service

import com.codacy.client.bitbucket.v1.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v1.{Repository, SimpleRepository, SshKey}
import play.api.libs.json.Json

class RepositoryServices(client: BitbucketClient) {

  /*
   * Gets the details of the repositories that the user owns or has at least read access to
   * Use this if you're looking for a full list of all of the repositories associated with a user
   */
  def getRepositories: RequestResponse[Seq[SimpleRepository]] = {
    client.execute(Request(s"https://bitbucket.org/api/1.0/user/repositories", classOf[Seq[SimpleRepository]]))
  }

  /*
   * Gets the list of the user's repositories. Private repositories only appear on this list
   * if the caller is authenticated and is authorized to view the repository.
   */
  def getRepositories(username: String): RequestResponse[Seq[Repository]] = {
    client.executePaginated(Request(s"https://bitbucket.org/api/2.0/repositories/$username", classOf[Seq[Repository]]))
  }

  /*
   * Creates a ssh key
   */
  def createKey(username: String, repo: String, key: String, label: String = "Codacy Key"): RequestResponse[SshKey] = {
    val url = s"https://bitbucket.org/api/1.0/repositories/$username/$repo/deploy-keys"

    val values = Json.obj(
      "key" -> key,
      "label" -> label
    )

    client.postJson(Request(url, classOf[SshKey]), values)
  }

}
