package com.codacy.client.bitbucket.v2

import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.Json

class WorkspaceSpecs extends FlatSpec with Matchers {

  "WorkspaceSpecs" should "successfully parse Projects" in {
    val input =
      """
        |      {
        |         "type":"<string>",
        |         "links":{
        |            "html":{
        |               "href":"<string>",
        |               "name":"<string>"
        |            },
        |            "avatar":{
        |               "href":"<string>",
        |               "name":"<string>"
        |            }
        |         },
        |         "uuid":"<string>",
        |         "key":"some key",
        |         "owner":{
        |            "type":"<string>"
        |         },
        |         "name":"Pluto",
        |         "description":"<string>",
        |         "is_private":true,
        |         "created_on":"<string>",
        |         "updated_on":"<string>",
        |         "has_publicly_visible_repos":true
        |      }
        |

      """.stripMargin

    val json = Json.parse(input)
    val value = json.validate[Project]

    value.fold(e => fail(s"$e"), p => {
      p.name shouldBe "Pluto"
      p.key shouldBe "some key"
    })
  }

}
