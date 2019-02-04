package com.codacy.client.bitbucket.v1

import org.scalatest.{Matchers, _}
import play.api.libs.json.Json

class BuildStatusSpecs extends FlatSpec with Matchers {

  "BuildStatusSpecs" should "successfully parse BuildStatus" in {
    val input =
      """
        |{
        |  "key": "bt28",
        |  "description": "Tests passed: 93, ignored: 1",
        |  "repository": {
        |    "links": {
        |      "self": {
        |        "href": "https://bitbucket.org/!api/2.0/repositories/example/test"
        |      },
        |      "html": {
        |        "href": "https://bitbucket.org/example/test"
        |      },
        |      "avatar": {
        |        "href": "https://bitbucket.org/example/test/avatar/32/"
        |      }
        |    },
        |    "type": "repository",
        |    "name": "codacy-website",
        |    "full_name": "example/test",
        |    "uuid": "{f6f653be-75ea-4dc9-84af-645a5ede93aa}"
        |  },
        |  "url": "https://www.example.com",
        |  "links": {
        |    "commit": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/014d431153a45e14afec424ecfe91005cf4d44b2"
        |    },
        |    "self": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/014d431153a45e14afec424ecfe91005cf4d44b2/statuses/build/bt28"
        |    }
        |  },
        |  "refname": null,
        |  "state": "SUCCESSFUL",
        |  "created_on": "2018-03-23T19:07:25.905540+00:00",
        |  "commit": {
        |    "hash": "014d431153a45e14afec424ecfe91005cf4d44b2",
        |    "type": "commit",
        |    "links": {
        |      "self": {
        |        "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/014d431153a45e14afec424ecfe91005cf4d44b2"
        |      },
        |      "html": {
        |        "href": "https://bitbucket.org/example/test/commits/014d431153a45e14afec424ecfe91005cf4d44b2"
        |      }
        |    }
        |  },
        |  "updated_on": "2018-03-23T19:23:07.522060+00:00",
        |  "type": "build",
        |  "name": "Build #4.0"
        |}
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[BuildStatus]

    value.fold(e => fail(s"$e"), r => r.key shouldBe "bt28")
  }

}
