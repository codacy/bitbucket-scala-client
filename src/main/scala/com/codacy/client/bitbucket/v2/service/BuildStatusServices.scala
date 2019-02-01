package com.codacy.client.bitbucket.v2.service

import com.codacy.client.bitbucket.v2.BuildStatus
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json._

class BuildStatusServices(client: BitbucketClient) {

  /*
   * Gets a commit build status
   *
   */
  def getBuildStatus(owner: String, repository: String, commit: String, key: String): RequestResponse[BuildStatus] = {
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/commit/$commit/statuses/build/$key"

    client.execute(Request(url, classOf[BuildStatus]))
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
    val url = s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/commit/$commit/statuses/build"

    val values = Map(
      "state" -> Seq(buildStatus.state.toString),
      "key" -> Seq(buildStatus.key),
      "name" -> Seq(buildStatus.name),
      "url" -> Seq(buildStatus.url),
      "description" -> Seq(buildStatus.description)
    )

    client.postForm(Request(url, classOf[BuildStatus]), values)
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
    val url =
      s"https://bitbucket.org/api/2.0/repositories/$owner/$repository/commit/$commit/statuses/build/${buildStatus.key}"

    val payload = Json.obj(
      "state" -> buildStatus.state,
      "name" -> buildStatus.name,
      "url" -> buildStatus.url,
      "description" -> buildStatus.description
    )

    client.putJson(Request(url, classOf[BuildStatus]), payload)
  }
}
