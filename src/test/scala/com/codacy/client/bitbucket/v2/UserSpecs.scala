package com.codacy.client.bitbucket.v2
import org.scalatest.{Matchers, _}
import play.api.libs.json.Json

class UserSpecs extends FlatSpec with Matchers {

  "UserSpecs" should "successfully parse a JSON into an array of Email" in {
    val input = """
                  |[
                  |  {
                  |    "is_primary": true,
                  |    "is_confirmed": true,
                  |    "type": "email",
                  |    "email": "mrfyda@gmail.com",
                  |    "links": {
                  |      "self": {
                  |        "href": "https://api.bitbucket.org/2.0/user/emails/mrfyda@gmail.com"
                  |      }
                  |    }
                  |  },
                  |  {
                  |    "is_primary": false,
                  |    "is_confirmed": true,
                  |    "type": "email",
                  |    "email": "rafael@qamine.com",
                  |    "links": {
                  |      "self": {
                  |        "href": "https://api.bitbucket.org/2.0/user/emails/rafael@qamine.com"
                  |      }
                  |    }
                  |  },
                  |  {
                  |    "is_primary": false,
                  |    "is_confirmed": true,
                  |    "type": "email",
                  |    "email": "pmaradas@gmail.com",
                  |    "links": {
                  |      "self": {
                  |        "href": "https://api.bitbucket.org/2.0/user/emails/pmaradas@gmail.com"
                  |      }
                  |    }
                  |  }
                  |]
                """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[Seq[Email]]

    value.fold(e =>
      fail(s"$e"),
      emails => emails.length shouldBe 3
    )
  }
}