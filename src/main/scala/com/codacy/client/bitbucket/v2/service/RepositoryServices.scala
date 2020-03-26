package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder

import com.codacy.client.bitbucket.v2.{DeployKey, OwnerInfo, Repository, Role}
import com.codacy.client.bitbucket.client.{BitbucketClient, PageRequest, Request, RequestResponse}
import play.api.libs.json.Json
import com.codacy.client.bitbucket.util.UrlHelper._

class RepositoryServices(client: BitbucketClient) {

  private val BaseUrl: String = "https://bitbucket.org/api/2.0/repositories"

  /**
    * Gets the list of the user's repositories. Private repositories only appear on this list
    * if the caller is authenticated and is authorized to view the repository.
    */
  def getRepositories(
      ownerInfo: OwnerInfo,
      pageLength: Option[Int] = Option(100),
      userRole: Option[Role] = None,
      pageRequest: Option[PageRequest] = None
  ): RequestResponse[Seq[Repository]] = {
    val encodedOwner = URLEncoder.encode(ownerInfo.value, "UTF-8")
    val baseUrl = s"$BaseUrl/${encodedOwner}"
    val role = userRole.fold("")(role => s"role=${role.value}")

    pageRequest match {
      case Some(request) =>
        val url = joinQueryParameters(baseUrl, role)
        client.executeWithCursor[Repository](url, request)
      case None =>
        val length = pageLength.fold("")(pagelen => s"pagelen=$pagelen")
        val url = joinQueryParameters(baseUrl, role, length)
        client.executePaginated(Request(url, classOf[Seq[Repository]]))
    }
  }

  /**
    * Retrieve the repository matching the supplied owner and repositorySlug.
    *
    * @param workspace The workspace ID (slug) or the workspace UUID surrounded by curly-braces.
    * @param repositorySlug The repository slug or the UUID of the repository surrounded by curly-braces
    * @return A [[RequestResponse]] with the repository data
    */
  def getRepository(workspace: String, repositorySlug: String): RequestResponse[Repository] = {
    val encodedWorkspace = URLEncoder.encode(workspace, "UTF-8")
    client.execute(Request(s"$BaseUrl/$encodedWorkspace/$repositorySlug", classOf[Repository]))
  }

  def createKey(
      username: String,
      repo: String,
      key: String,
      label: String = "Codacy Key"
  ): RequestResponse[DeployKey] = {
    val url = s"$BaseUrl/$username/$repo/deploy-keys"

    val values = Json.obj("key" -> key, "label" -> label)

    client.postJson(Request(url, classOf[DeployKey]), values)
  }
}
