package com.codacy.client.bitbucket.v1.service

import com.codacy.client.bitbucket.v1.Webhook
import com.codacy.client.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.Json

class HookServices(client: BitbucketClient) {

  def list(author: String, repo: String): RequestResponse[Seq[Webhook]] = {
    val servicesUrl = getServicesUrl(author, repo)
    client.executePaginated(Request(servicesUrl, classOf[Seq[Webhook]]))
  }

  def create(author: String, repo: String, description: String, hookUrl: String, events:Set[String]): RequestResponse[Webhook] = {
    val servicesUrl = getServicesUrl(author, repo)
    val payload = Json.obj(
      "active"      -> true,
      "description" -> description,
      "url"         -> hookUrl,
      "events"      -> events
    )
    client.postJson(Request(servicesUrl, classOf[Webhook]), payload)
  }

  def update(author: String, repo: String, uuid: String,
    active: Boolean, description: String, hookUrl: String, events:Set[String]): RequestResponse[Webhook] = {
    val servicesUrl = getServicesUrl(author, repo)
    val payload = Json.obj(
      "active"      -> active,
      "description" -> description,
      "url"         -> hookUrl,
      "events"      -> events
    )
    client.putJson(Request(s"$servicesUrl/$uuid", classOf[Webhook]), payload)
  }

  def delete(author: String, repo: String, uuid: String): RequestResponse[Boolean] = {
    val servicesUrl = getServicesUrl(author, repo)
    client.delete(s"$servicesUrl/$uuid")
  }

  private[this] lazy val BASE_URL: String = "https://api.bitbucket.org/2.0/repositories"

  private[this] def getServicesUrl(owner: String, repo: String) = s"$BASE_URL/$owner/$repo/hooks"

}
