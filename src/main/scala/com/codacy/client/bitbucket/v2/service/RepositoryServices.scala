package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.v2.{DeployKey, OwnerInfo, Repository, Role}
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.Json
import com.codacy.client.bitbucket.util.UrlHelper._

class RepositoryServices(client: BitbucketClient) {

  /*
   * Gets the list of the user's repositories. Private repositories only appear on this list
   * if the caller is authenticated and is authorized to view the repository.
   */
  def getRepositories(
      ownerInfo: OwnerInfo,
      pageLength: Option[Int] = Option(100),
      userRole: Option[Role] = None
  ): RequestResponse[Seq[Repository]] = {
    val baseUrl = s"https://bitbucket.org/api/2.0/repositories/${ownerInfo.value}"
    val role = userRole.fold("")(role => s"role=${role.value}")
    val lenght = pageLength.fold("")(pagelen => s"pagelen=$pagelen")
    val queryParameters = List(role, lenght)
    val url = joinQueryParameters(baseUrl, role, lenght)
    client.executePaginated(Request(url, classOf[Seq[Repository]]))
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
