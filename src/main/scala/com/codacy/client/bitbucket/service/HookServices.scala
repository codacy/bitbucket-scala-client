package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.Service
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}

class ServiceServices(client: BitbucketClient) {

  def list(author: String, repo: String): RequestResponse[Seq[Service]] =
    AuthorRepoOps(author,repo).list()

  /*def byId(author: String, repo: String, id:Long) = {
    val resp = AuthorRepoOps(author,repo).list()
    val value = resp.value.map(_.find(_.id == id))
    resp.copy(value = value)
  }*/

  def created(author: String, repo: String, payload: Map[String,Seq[String]]) =
    AuthorRepoOps(author,repo).created(payload)

  def removed(author: String, repo: String, id:Long) =
    AuthorRepoOps(author,repo).removed(id)

  private[this] lazy val BASE_URL: String = "https://bitbucket.org/!api/1.0/repositories"
  private[this] def servicesUrl(author: String, repo: String) = s"$BASE_URL/$author/$repo/services"

  private[this] case class AuthorRepoOps(author: String, repo: String){
    private[this] lazy val BASE_URL = servicesUrl(author,repo)

    def created(payload: Map[String,Seq[String]]): RequestResponse[Service] =
      client.post(Request(BASE_URL,classOf[Service]),payload)

    def removed(id:Long): RequestResponse[Boolean] =
      client.delete(Request(s"%BASE_URL/$id",classOf[Boolean]))

    def list(): RequestResponse[Seq[Service]] =
      client.execute(Request(BASE_URL,classOf[Seq[Service]]))
  }
}
