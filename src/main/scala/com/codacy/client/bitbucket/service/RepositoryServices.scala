package com.codacy.client.bitbucket.service

import com.codacy.client.bitbucket.Repository
import com.codacy.client.bitbucket.client.{BitbucketClient, Request, RequestResponse}

class RepositoryServices(client: BitbucketClient) {

  /*
   * Gets the list of the user's repositories. Private repositories only appear on this list
   * if the caller is authenticated and is authorized to view the repository.
   */
  def getRepositories(username: String): RequestResponse[Seq[Repository]] = {
    client.executePaginated(Request(s"https://bitbucket.org/api/2.0/repositories/$username", classOf[Seq[Repository]]))
  }

}
