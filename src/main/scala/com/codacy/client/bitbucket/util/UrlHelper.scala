package com.codacy.client.bitbucket.util

object UrlHelper {

  def joinQueryParameters(baseUrl: String, queryParameters: String*): String = {
    val parameters = queryParameters.mkString("&")
    if (parameters.nonEmpty) {
      baseUrl match {
        case url if url.contains('?') =>
          url + "&" + parameters
        case url =>
          url + "?" + parameters
      }
    } else {
      baseUrl
    }
  }
}
