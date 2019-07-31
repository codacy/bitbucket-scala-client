package com.codacy.client.bitbucket.util

import org.scalatest.{FlatSpec, Matchers}
import com.codacy.client.bitbucket.util.UrlHelper._

class UrlHelperSpecs extends FlatSpec with Matchers {

  "UrlHelperSpecs" should "add multiple query parameters to base url" in {
    val baseUrl = "api.bitbucket.com"
    val pagelenParameter = "pagelen=5"
    val roleParameter = "role=admin"
    val expectedUrl = s"$baseUrl?pagelen=5&role=admin"
    val resultUrl = joinQueryParameters(baseUrl, pagelenParameter, roleParameter)

    resultUrl shouldBe expectedUrl
  }

  "UrlHelperSpecs" should "add one query parameter to base url" in {
    val baseUrl = "api.bitbucket.com"
    val pagelenParameter = "pagelen=5"
    val expectedUrl = s"$baseUrl?pagelen=5"
    val resultUrl = joinQueryParameters(baseUrl, pagelenParameter)

    resultUrl shouldBe expectedUrl
  }
}
