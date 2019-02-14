package com.codacy.client.bitbucket.v2
import org.scalatest.{Matchers, _}
import play.api.libs.json.Json

class UserSpecs extends FlatSpec with Matchers with Inside {

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

    value.fold(e => fail(s"$e"), emails => emails.length shouldBe 3)
  }
  it should "successfully parse a JSON into an array of Team" in {
    val input = """
                  |[
                  |{
                  | "permission": "collaborator",
                  | "type": "team_permission",
                  | "user": {
                  | "username": "jllopes",
                  | "display_name": "João Lopes",
                  | "account_id": "123asdfas87afsd8f9asd7as",
                  | "links": {
                  |  "self": {
                  |   "href": "https://bitbucket.org/!api/2.0/users/jllopes"
                  |  },
                  |  "html": {
                  |    "href": "https://bitbucket.org/jllopes/"
                  |  },
                  |  "avatar": {
                  |   "href": "https://bitbucket.org/account/jllopes/avatar/"
                  |  }
                  | },
                  | "nickname": "jllopes",
                  | "type": "user",
                  | "uuid": "{42417371-64fe-4cde-b528-b7454e8a0aaf}"
                  |},
                  | "team": {
                  |   "username": "testteam1",
                  |   "display_name": "TestTeam1",
                  |   "type": "team",
                  |   "uuid": "{85ea8027-2a7f-4094-828d-f20c935d373a}",
                  |   "links": {
                  |     "self": {
                  |       "href": "https://bitbucket.org/!api/2.0/teams/testteam1"
                  |     },
                  |     "html": {
                  |       "href": "https://bitbucket.org/testteam1/"
                  |     },
                  |     "avatar": {
                  |       "href": "https://bitbucket.org/account/testteam1/avatar/"
                  |     }
                  |   }
                  | }
                  |},
                  |{
                  | "permission": "collaborator",
                  | "type": "team_permission",
                  | "user": {
                  |   "username": "jllopes",
                  |   "display_name": "João Lopes",
                  |   "account_id": "123asdfas87afsd8f9asd7as",
                  |   "links": {
                  |     "self": {
                  |       "href": "https://bitbucket.org/!api/2.0/users/jllopes"
                  |     },
                  |     "html": {
                  |       "href": "https://bitbucket.org/jllopes/"
                  |     },
                  |     "avatar": {
                  |       "href": "https://bitbucket.org/account/jllopes/avatar/"
                  |     }
                  |   },
                  | "nickname": "jllopes",
                  | "type": "user",
                  | "uuid": "{42417371-64fe-4cde-b528-b7454e8a0aaf}"
                  |},
                  | "team": {
                  |   "username": "testteam2",
                  |   "display_name": "TestTeam2",
                  |   "type": "team",
                  |   "uuid": "{7ec1171b-3df1-4c00-bfcf-2d4eda99e44a}",
                  |   "links": {
                  |     "self": {
                  |       "href": "https://bitbucket.org/!api/2.0/teams/testteam2"
                  |     },
                  |     "html": {
                  |       "href": "https://bitbucket.org/testteam2/"
                  |     },
                  |     "avatar": {
                  |       "href": "https://bitbucket.org/account/testteam2/avatar/"
                  |     }
                  |   }
                  | }
                  |}
                  |]
                """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[Seq[Team]]

    value.fold(e => fail(s"$e"), teams => teams.length shouldBe 2)
  }
  it should "successfully parse a JSON into a User" in {
    val input =
      """
          |{
          | "username":"jllopes",
          | "website":"",
          | "display_name":"João Lopes",
          | "account_id":"123abc456def789ghi101jkl",
          | "links":
          | {
          |   "hooks":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/users\/jllopes\/hooks"
          |   },
          |   "self":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/users\/jllopes"
          |   },
          |   "repositories":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes"
          |   },
          |   "html":
          |   {
          |     "href":"https:\/\/bitbucket.org\/jllopes\/"
          |   },
          |   "followers":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/users\/jllopes\/followers"
          |   },
          |   "avatar":
          |   {
          |     "href":"https:\/\/bitbucket.org\/account\/jllopes\/avatar\/"
          |   },
          |   "following":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/users\/jllopes\/following"
          |   },
          |   "snippets":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/snippets\/jllopes"
          |   }
          | },
          | "type":"user",
          | "created_on":"2018-07-02T10:41:55.342788+00:00",
          | "is_staff":false,
          | "location":null,
          | "account_status":"active",
          | "nickname":"jllopes",
          | "uuid":"{c19f822b-0e29-433a-87a5-ec8ace58aa67}"
          |}""".stripMargin
    val json = Json.parse(input)
    val value = json.validate[User]

    value.fold(e => fail(s"$e"), user => inside(user) {
      case User(account_id, uuid, display_name, avatarUrl) =>
        account_id shouldBe "123abc456def789ghi101jkl"
        uuid shouldBe "{c19f822b-0e29-433a-87a5-ec8ace58aa67}"
        display_name shouldBe "João Lopes"
        avatarUrl.get shouldBe "https://bitbucket.org/account/jllopes/avatar/"
    })
  }
}
