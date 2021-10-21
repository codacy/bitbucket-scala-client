package com.codacy.client.bitbucket.client

import org.scalatest.{EitherValues, Matchers, OptionValues, WordSpec}

class PageRequestSpec extends WordSpec with Matchers with EitherValues with OptionValues {

  "apply" should {
    "succeed when given a valid cursor that matches the api host" in {
      //ARRANGE
      val apiUrl = "http://wubba.lubba.com/dub/dub"
      val cursorValue = "http://wubba.lubba.com/dub/dub/mr/nimbus"

      //ACT
      val actualResult = PageRequest(Some(cursorValue), apiUrl)

      //ASSERT
      actualResult.right.value.cursor.value should equal(cursorValue)
    }
    "succeed when given a None cursor" in {
      //ARRANGE
      val apiUrl = "http://wubba.lubba.com/dub/dub"

      //ACT
      val actualResult = PageRequest(None, apiUrl)

      //ASSERT
      actualResult.right.value.cursor shouldBe None
    }
    "fail when the api url is malformed" in {
      //ARRANGE
      val apiUrl = "i'm not a url"
      val cursorValue = "http://wubba.lubba.com/dub/dub/mr/nimbus"

      //ACT
      val actualResult = PageRequest(Some(cursorValue), apiUrl)

      //ASSERT
      actualResult.left.value should equal(s"""Invalid api url $apiUrl""")
    }
    "fail when the cursor value is malformed" in {
      //ARRANGE
      val apiUrl = "http://wubba.lubba.com/dub/dub/"
      val cursorValue = "i'm not a url"

      //ACT
      val actualResult = PageRequest(Some(cursorValue), apiUrl)

      //ASSERT
      actualResult.left.value should equal(s"""Value "$cursorValue" is not a valid cursor: Malformed URL""")
    }
    "fail when the cursor does not match the same host as the api host" in {
      //ARRANGE
      val apiUrl = "http://wubba.lubba.com/dub/dub/"
      val cursorValue = "http://not.the.api.host/dub/dub"

      //ACT
      val actualResult = PageRequest(Some(cursorValue), apiUrl)

      //ASSERT
      actualResult.left.value should equal(
        s"""Value "$cursorValue" is not a valid cursor: Hostname does not match Bitbucket Server API"""
      )
    }
  }
}
