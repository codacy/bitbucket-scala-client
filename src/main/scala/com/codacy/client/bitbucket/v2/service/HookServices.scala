package com.codacy.client.bitbucket.v2.service

import java.net.URLEncoder

import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import com.codacy.client.bitbucket.v2
import play.api.libs.json.Json

class HookServices(client: BitbucketClient) {

  def list(author: String, repo: String): RequestResponse[Seq[v2.Webhook]] = {
    val servicesUrl = generateHooksUrl(author, repo)
    client.executePaginated(Request(servicesUrl, classOf[Seq[v2.Webhook]]))
  }

  def create(
      author: String,
      repo: String,
      description: String,
      hookUrl: String,
      events: Set[String]
  ): RequestResponse[v2.Webhook] = {
    val servicesUrl = generateHooksUrl(author, repo)
    val payload = Json.obj("active" -> true, "description" -> description, "url" -> hookUrl, "events" -> events)
    client.postJson(Request(servicesUrl, classOf[v2.Webhook]), payload)
  }

  def update(
      author: String,
      repo: String,
      uuid: String,
      active: Boolean,
      description: String,
      hookUrl: String,
      events: Set[String]
  ): RequestResponse[v2.Webhook] = {
    val servicesUrl = generateHooksUrl(author, repo)
    val encodedUuid = URLEncoder.encode(uuid, "UTF-8")
    val payload = Json.obj("active" -> active, "description" -> description, "url" -> hookUrl, "events" -> events)
    client.putJson(Request(s"$servicesUrl/$encodedUuid", classOf[v2.Webhook]), payload)
  }

  def delete(author: String, repo: String, uuid: String): RequestResponse[Boolean] = {
    val servicesUrl = generateHooksUrl(author, repo)
    val encodedUuid = URLEncoder.encode(uuid, "UTF-8")
    client.delete(s"$servicesUrl/$encodedUuid")
  }

  private def generateHooksUrl(owner: String, repo: String): String = {
    val encodedOwner = URLEncoder.encode(owner, "UTF-8")
    val encodedRepo = URLEncoder.encode(repo, "UTF-8")
    s"${client.repositoriesBaseUrl}/$encodedOwner/$encodedRepo/hooks"
  }

}
