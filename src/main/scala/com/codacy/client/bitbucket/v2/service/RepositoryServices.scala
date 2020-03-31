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
    *
    * @param ownerInfo The username or the UUID of the account surrounded by curly-braces
    * @param pageLength The number of items of the page to be returned
    * @param userRole The role of the user to filter the repositories
    * @param pageRequest The cursor to get a page of repositories
    * @param sortBy The name of the field to sort the repositories. By default it is done in ascending order and
    *               it should be used an hyphen to reverse the order. Also, by default is ordered by last updated date
    * @param repositorySlug The slug of the repository to search the repository.
    *                       It finds any case-insensitive text that contains this string
    * @return a [[RequestResponse]] with a sequence of repositories
    */
  def getRepositories(
      ownerInfo: OwnerInfo,
      pageLength: Option[Int] = Option(100),
      userRole: Option[Role] = None,
      pageRequest: Option[PageRequest] = None,
      sortBy: Option[String] = Option("-updated_on"),
      repositorySlug: Option[String] = None
  ): RequestResponse[Seq[Repository]] = {
    val encodedOwner = URLEncoder.encode(ownerInfo.value, "UTF-8")
    val baseUrl = s"$BaseUrl/$encodedOwner"
    val role = userRole.fold("")(role => s"role=${role.value}")
    val sort = sortBy.fold("")(sortField => s"sort=$sortField")
    val length = pageLength.fold("")(pagelen => s"pagelen=$pagelen")
    val filterRepositorySlug =
      repositorySlug.fold("") { slugField =>
        val encodedSlugField = URLEncoder.encode(s""""$slugField"""", "UTF-8")
        s"""q=slug~$encodedSlugField"""
      }

    val url = joinQueryParameters(baseUrl, role, sort, length, filterRepositorySlug)

    pageRequest match {
      case Some(request) =>
        client.executeWithCursor[Repository](url, request)
      case None =>
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
