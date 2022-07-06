package com.codacy.client.bitbucket.v2

import org.scalatest.{Matchers, _}
import play.api.libs.json.Json

class IssueSpecs extends FlatSpec with Matchers {

  "IssueSpecs" should "successfully parse Issue" in {
    val input =
      """
        |{
        |    "content": {
        |        "raw": "",
        |        "markup": "markdown",
        |        "html": "",
        |        "type": "rendered"
        |    },
        |    "kind": "task",
        |    "repository": {
        |        "name": "test",
        |        "type": "repository",
        |        "full_name": "test/test",
        |        "links": {
        |            "self": {
        |                "href": "https://bitbucket.org/!api/2.0/repositories/test/test"
        |            },
        |            "html": {
        |                "href": "https://bitbucket.org/test/test"
        |            },
        |            "avatar": {
        |                "href": "https://bytebucket.org/ravatar/%12345667-d7b9-41c1-9d19-35d012423d0d%7D?ts=default"
        |            }
        |        },
        |        "uuid": "c19f822b-0e29-433a-87a5-ec8ace58aa67"
        |    },
        |    "links": {
        |        "attachments": {
        |            "href": "https://bitbucket.org/!api/2.0/repositories/test/test/issues/7/attachments"
        |        },
        |        "self": {
        |            "href": "https://bitbucket.org/!api/2.0/repositories/test/test/issues/7"
        |        },
        |        "watch": {
        |            "href": "https://bitbucket.org/!api/2.0/repositories/test/test/issues/7/watch"
        |        },
        |        "comments": {
        |            "href": "https://bitbucket.org/!api/2.0/repositories/test/test/issues/7/comments"
        |        },
        |        "html": {
        |            "href": "https://bitbucket.org/test/test/issues/7/add-code-coverage-to-react"
        |        },
        |        "vote": {
        |            "href": "https://bitbucket.org/!api/2.0/repositories/test/test/issues/7/vote"
        |        }
        |    },
        |    "title": "Add code coverage to React",
        |    "reporter": {
        |        "username": "reporter",
        |        "display_name": "reporter",
        |        "links": {
        |            "self": {
        |                "href": "https://bitbucket.org/!api/2.0/users/reporter"
        |            },
        |            "html": {
        |                "href": "https://bitbucket.org/reporter/"
        |            },
        |            "avatar": {
        |                "href": "https://bitbucket.org/account/reporter/avatar/"
        |            }
        |        },
        |        "type": "user",
        |        "nickname": "reporter",
        |        "uuid": "c19f822b-0e29-433a-87a5-ec8ace58aa67"
        |    },
        |    "component": {
        |        "name": "React",
        |        "links": {
        |            "self": {
        |                "href": "https://bitbucket.org/!api/2.0/repositories/test/test/components/497198"
        |            }
        |        }
        |    },
        |    "votes": 0,
        |    "watches": 1,
        |    "priority": "major",
        |    "assignee": {
        |        "username": "jllopes",
        |        "display_name": "João Lopes",
        |        "links": {
        |            "self": {
        |                "href": "https://bitbucket.org/!api/2.0/users/jllopes"
        |            },
        |            "html": {
        |                "href": "https://bitbucket.org/jllopes/"
        |            },
        |            "avatar": {
        |                "href": "https://bitbucket.org/account/jllopes/avatar/"
        |            }
        |        },
        |        "type": "user",
        |        "nickname": "jllopes",
        |        "uuid": "c19f822b-0e29-433a-87a5-ec8ace58aa67"
        |    },
        |    "state": "resolved",
        |    "version": null,
        |    "edited_on": null,
        |    "created_on": "2018-10-01T15:25:53.034773+00:00",
        |    "milestone": null,
        |    "updated_on": "2018-10-03T13:19:06.657709+00:00",
        |    "type": "issue",
        |    "id": 7
        |}
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[Issue]

    value.fold(e => fail(s"$e"), r => {
      r.id shouldBe 7
      r.reporter.uuid shouldBe "c19f822b-0e29-433a-87a5-ec8ace58aa67"
    })
  }

}
