package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.Service
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}
import play.api.libs.json.Json

class HookServices(client: BitbucketClient) {

  def list(author: String, repo: String): RequestResponse[Seq[Service]] = {
    val servicesUrl = getServicesUrl(author, repo)
    client.execute(Request(servicesUrl, classOf[Seq[Service]]))
  }

  def create(author: String, repo: String, hookType: String, hookUrl: String): RequestResponse[Service] = {
    val servicesUrl = getServicesUrl(author, repo)
    val payload = Json.obj(
      "type" -> hookType,
      "URL" -> hookUrl
    )
    client.post(Request(servicesUrl, classOf[Service]), payload)
  }

  def delete(author: String, repo: String, id: Long): RequestResponse[Boolean] = {
    val servicesUrl = getServicesUrl(author, repo)
    client.delete(s"$servicesUrl/$id")
  }

  private lazy val BASE_URL: String = "https://bitbucket.org/!api/1.0/repositories"

  private def getServicesUrl(author: String, repo: String) = s"$BASE_URL/$author/$repo/services"

}
