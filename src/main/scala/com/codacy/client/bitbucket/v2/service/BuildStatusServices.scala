package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder

import com.codacy.client.bitbucket.client.DefaultBodyWritables._
import com.codacy.client.bitbucket.client.{BitbucketClient, RequestResponse}
import com.codacy.client.bitbucket.v2.BuildStatus
import play.api.libs.json.Json

class BuildStatusServices(client: BitbucketClient) {

  /*
   * Gets a commit build status
   *
   */
  def getBuildStatus(owner: String, repository: String, commit: String, key: String): RequestResponse[BuildStatus] = {
    val commitBuildStatusesUrl = generateCommitBuildStatusesUrl(owner, repository, commit)
    val encodedKey = URLEncoder.encode(key, "UTF-8")
    val url = s"$commitBuildStatusesUrl/$encodedKey"

    client.execute[BuildStatus](url)
  }

  /*
   * Create a build status for a commit
   *
   */
  def createBuildStatus(
      owner: String,
      repository: String,
      commit: String,
      buildStatus: BuildStatus
  ): RequestResponse[BuildStatus] = {
    val commitBuildStatusesUrl = generateCommitBuildStatusesUrl(owner, repository, commit)

    val values = Map(
      "state" -> Seq(buildStatus.state.toString),
      "key" -> Seq(buildStatus.key),
      "name" -> Seq(buildStatus.name),
      "url" -> Seq(buildStatus.url),
      "description" -> Seq(buildStatus.description)
    )

    client.postForm[Map[String, Seq[String]], BuildStatus](commitBuildStatusesUrl, values)
  }

  /*
   * Update a build status for a commit
   *
   */
  def updateBuildStatus(
      owner: String,
      repository: String,
      commit: String,
      buildStatus: BuildStatus
  ): RequestResponse[BuildStatus] = {
    val commitBuildStatusesUrl = generateCommitBuildStatusesUrl(owner, repository, commit)
    val encodedBuildStatusKey = URLEncoder.encode(buildStatus.key, "UTF-8")
    val url = s"$commitBuildStatusesUrl/$encodedBuildStatusKey"

    val payload = Json.obj(
      "state" -> buildStatus.state,
      "name" -> buildStatus.name,
      "url" -> buildStatus.url,
      "description" -> buildStatus.description
    )

    client.putJson[BuildStatus](url, payload)
  }

  private def generateCommitBuildStatusesUrl(owner: String, repository: String, commit: String): String = {
    val encodedOwner = URLEncoder.encode(owner, "UTF-8")
    val encodedRepository = URLEncoder.encode(repository, "UTF-8")
    val encodedCommit = URLEncoder.encode(commit, "UTF-8")
    s"${client.repositoriesBaseUrl}/$encodedOwner/$encodedRepository/commit/$encodedCommit/statuses/build"
  }
}
