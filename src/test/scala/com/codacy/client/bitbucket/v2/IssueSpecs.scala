package com.codacy.client.bitbucket.v2

import org.scalatest.{Matchers, _}
import play.api.libs.json.Json

class IssueSpecs extends FlatSpec with Matchers {

  "IssueSpecs" should "successfully parse Issue" in {
    val input =
      """
        |{
        |	"priority": "major",
        |	"kind": "bug",
        |	"repository": {
        |		"links": {
        |			"self": {
        |				"href": "https://bitbucket.org/!api/2.0/repositories/mrfyda/scalatests"
        |			},
        |			"html": {
        |				"href": "https://bitbucket.org/mrfyda/scalatests"
        |			},
        |			"avatar": {
        |				"href": "https://bytebucket.org/ravatar/%7B75411621-5a51-4bd4-a982-1d86cedd309e%7D?ts=default"
        |			}
        |		},
        |		"type": "repository",
        |		"name": "ScalaTests",
        |		"full_name": "mrfyda/scalatests",
        |		"uuid": "{75411621-5a51-4bd4-a982-1d86cedd309e}"
        |	},
        |	"links": {
        |		"attachments": {
        |			"href": "https://bitbucket.org/!api/2.0/repositories/mrfyda/scalatests/issues/1/attachments"
        |		},
        |		"self": {
        |			"href": "https://bitbucket.org/!api/2.0/repositories/mrfyda/scalatests/issues/1"
        |		},
        |		"watch": {
        |			"href": "https://bitbucket.org/!api/2.0/repositories/mrfyda/scalatests/issues/1/watch"
        |		},
        |		"comments": {
        |			"href": "https://bitbucket.org/!api/2.0/repositories/mrfyda/scalatests/issues/1/comments"
        |		},
        |		"html": {
        |			"href": "https://bitbucket.org/mrfyda/scalatests/issues/1/2e95d690c2c58a72e1a0bb4cd76808a4c0c60db4"
        |		},
        |		"vote": {
        |			"href": "https://bitbucket.org/!api/2.0/repositories/mrfyda/scalatests/issues/1/vote"
        |		}
        |	},
        |	"reporter": {
        |		"username": "mrfyda",
        |		"display_name": "Rafael Cortês",
        |		"account_id": "557058:cb3153a1-1a69-4374-a536-fefac8f1cc64",
        |		"links": {
        |			"self": {
        |				"href": "https://bitbucket.org/!api/2.0/users/mrfyda"
        |			},
        |			"html": {
        |				"href": "https://bitbucket.org/mrfyda/"
        |			},
        |			"avatar": {
        |				"href": "https://bitbucket.org/account/mrfyda/avatar/"
        |			}
        |		},
        |		"type": "user",
        |		"uuid": "{1e960327-38ba-4100-919e-5a677e668cad}"
        |	},
        |	"title": "2e95d690c2c58a72e1a0bb4cd76808a4c0c60db4",
        |	"component": null,
        |	"votes": 0,
        |	"watches": 1,
        |	"content": {
        |		"raw": "hello world",
        |		"markup": "markdown",
        |		"html": "<p>hello world</p>",
        |		"type": "rendered"
        |	},
        |	"assignee": null,
        |	"state": "new",
        |	"version": null,
        |	"edited_on": null,
        |	"created_on": "2018-09-02T09:08:01.435083+00:00",
        |	"milestone": null,
        |	"updated_on": "2018-09-02T09:08:01.435083+00:00",
        |	"type": "issue",
        |	"id": 1
        |}
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[Issue]

    value.fold(e => fail(s"$e"), r => r.id shouldBe 1)
  }

}
