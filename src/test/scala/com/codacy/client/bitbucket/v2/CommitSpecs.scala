package com.codacy.client.bitbucket.v2

import com.codacy.client.bitbucket.TestUtils
import org.scalatest._
import play.api.libs.json.{JsError, JsSuccess, Json}

class CommitSpecs extends WordSpec with Matchers {

  "Reads[BaseCommit]" should {

    "successfully parse default bitbucket response" in {
      val rawJson = TestUtils.getTestContent("/test-files/get_a_commit_1.json")
      val json = Json.parse(rawJson)
      val value = json.validate[BaseCommit]

      val expectedAvatarLink =
        "https://avatar-management--avatars.us-west-2.prod.public.atl-paas.net/557058:3aae1e05-702a-41e5-81c8-f36f29afb6ca/613070db-28b0-421f-8dba-ae8a87e2a5c7/128"

      value match {
        case JsSuccess(value, _) => assert(value.author.user.flatMap(_.avatarLink).contains(expectedAvatarLink))
        case JsError(errors) => fail(s"Failed with$errors")
      }
    }

    "successfully parse default remote-provider-service integration-test-repo1 repository commit" in {
      val rawJson = TestUtils.getTestContent("/test-files/get_a_commit_2.json")
      val value = Json.parse(rawJson).validate[BaseCommit]

      value match {
        case JsSuccess(value, _) => assert(value.author.user.flatMap(_.nickname).contains("integrationtest"))
        case JsError(errors) => fail(s"Failed with$errors")
      }
    }
  }
}
