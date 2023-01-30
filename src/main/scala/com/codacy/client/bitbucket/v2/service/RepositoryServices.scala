package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder

import com.codacy.client.bitbucket.client.{BitbucketClient, PageRequest, Request, RequestResponse, SuccessfulResponse}
import com.codacy.client.bitbucket.util.UrlHelper._
import com.codacy.client.bitbucket.v2._
import play.api.libs.json.{JsValue, Json}

class RepositoryServices(client: BitbucketClient) {

  private val DEFAULT_PAGE_LENGTH = 100

  /**
    * Gets the list of the user's repositories. Private repositories only appear on this list
    * if the caller is authenticated and is authorized to view the repository.
    *
    * @param ownerInfo The username or the UUID of the account surrounded by curly-braces
    * @param pageLength The number of items of the page to be returned, if None defaults to 100
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
      pageLength: Option[Int] = Option(DEFAULT_PAGE_LENGTH),
      userRole: Option[Role] = None,
      pageRequest: Option[PageRequest] = None,
      sortBy: Option[String] = Option("-updated_on"),
      repositorySlug: Option[String] = None
  ): RequestResponse[Seq[Repository]] = {
    val encodedOwner = URLEncoder.encode(ownerInfo.value, "UTF-8")
    val baseUrl = s"${client.repositoriesBaseUrl}/$encodedOwner"
    val role = userRole.fold("")(role => s"role=${role.value}")
    val sort = sortBy.fold("")(sortField => s"sort=$sortField")
    //Defaults to 10 if not set. We set our own default here instead to keep consistency with other providers
    val length = s"pagelen=${pageLength.getOrElse(DEFAULT_PAGE_LENGTH)}"
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
    val repositoryUrl = generateRepositoryUrl(workspace, repositorySlug)
    client.execute(Request(repositoryUrl, classOf[Repository]))
  }

  def createKey(
      username: String,
      repositorySlug: String,
      key: String,
      label: String = "Codacy Key"
  ): RequestResponse[DeployKey] = {
    val repositoryUrl = generateRepositoryUrl(username, repositorySlug)
    val url = s"$repositoryUrl/deploy-keys"

    val values = Json.obj("key" -> key, "label" -> label)

    client.postJson(Request(url, classOf[DeployKey]), values)
  }

  /**
    * Checks if username has repository:admin permission by calling the /branch-restriction endpoint
    * https://developer.atlassian.com/cloud/bitbucket/rest/api-group-branch-restrictions/#api-repositories-workspace-repo-slug-branch-restrictions-get
    *
    * @param username The username or the UUID of the account surrounded by curly-braces
    * @param repositorySlug The repository slug or the UUID of the repository surrounded by curly-braces
    */
  def hasRepositoryAdminPermission(username: String, repositorySlug: String): Boolean = {
    val repositoryUrl = generateRepositoryUrl(username, repositorySlug)
    val baseRequestUrl = s"$repositoryUrl/branch-restrictions"

    val response = client.execute(Request(baseRequestUrl, classOf[JsValue]))

    response match {
      case _: SuccessfulResponse[_] => true
      case _ => false
    }
  }

  private def generateRepositoryUrl(owner: String, repo: String): String = {
    val encodedOwner = URLEncoder.encode(owner, "UTF-8")
    val encodedRepo = URLEncoder.encode(repo, "UTF-8")
    s"${client.repositoriesBaseUrl}/$encodedOwner/$encodedRepo"
  }
}
