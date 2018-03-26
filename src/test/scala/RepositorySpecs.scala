import com.codacy.client.bitbucket.{Repository, SimpleRepository}
import org.scalatest.{Matchers, _}
import play.api.libs.json.Json

class RepositorySpecs extends FlatSpec with Matchers {

  "RepositorySpecs" should "successfully parse Repository" in {
    val input =
      """
        |{
        |  "scm": "git",
        |  "website": "",
        |  "has_wiki": true,
        |  "name": "Example",
        |  "links": {
        |    "watchers": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/example/watchers"
        |    },
        |    "branches": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/example/refs/branches"
        |    },
        |    "tags": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/example/refs/tags"
        |    },
        |    "commits": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/example/commits"
        |    },
        |    "clone": [{
        |      "href": "https://mrfyda@bitbucket.org/example/example.git",
        |      "name": "https"
        |    }, {
        |      "href": "git@bitbucket.org:example/example.git",
        |      "name": "ssh"
        |    }],
        |    "self": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/example"
        |    },
        |    "source": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/example/src"
        |    },
        |    "html": {
        |      "href": "https://bitbucket.org/example/example"
        |    },
        |    "avatar": {
        |      "href": "https://bitbucket.org/example/example/avatar/32/"
        |    },
        |    "hooks": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/example/hooks"
        |    },
        |    "forks": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/example/forks"
        |    },
        |    "downloads": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/example/downloads"
        |    },
        |    "pullrequests": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/example/pullrequests"
        |    }
        |  },
        |  "fork_policy": "no_public_forks",
        |  "uuid": "{7326e8f8-2250-41b2-a01d-3c605f4bf3d6}",
        |  "project": {
        |    "key": "TRAS",
        |    "type": "project",
        |    "uuid": "{c89a4e94-c301-47d2-93e6-e3353a57d8f2}",
        |    "links": {
        |      "self": {
        |        "href": "https://bitbucket.org/!api/2.0/teams/example/projects/TRAS"
        |      },
        |      "html": {
        |        "href": "https://bitbucket.org/account/user/example/projects/TRAS"
        |      },
        |      "avatar": {
        |        "href": "https://bitbucket.org/account/user/example/projects/TRAS/avatar/32"
        |      }
        |    },
        |    "name": "Trash"
        |  },
        |  "language": "scala",
        |  "created_on": "2013-08-16T09:06:25.403035+00:00",
        |  "mainbranch": {
        |    "type": "branch",
        |    "name": "dev"
        |  },
        |  "full_name": "example/example",
        |  "has_issues": false,
        |  "owner": {
        |    "username": "example",
        |    "display_name": "Example",
        |    "type": "team",
        |    "uuid": "{c08af10a-b49f-4a7a-9edb-6a66b3495a6f}",
        |    "links": {
        |      "self": {
        |        "href": "https://bitbucket.org/!api/2.0/teams/example"
        |      },
        |      "html": {
        |        "href": "https://bitbucket.org/example/"
        |      },
        |      "avatar": {
        |        "href": "https://bitbucket.org/account/example/avatar/32/"
        |      }
        |    }
        |  },
        |  "updated_on": "2016-02-04T19:08:54.473972+00:00",
        |  "size": 269498081,
        |  "type": "repository",
        |  "slug": "example",
        |  "is_private": true,
        |  "description": "Arrrr!"
        |}
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[Repository]

    value.fold(e =>
      fail(s"$e"),
      r => r.name shouldBe "Example"
    )
  }

  it should "successfully parse a JSON into an array of SimpleRepository" in {

    val input =
      """[
        |{
        |  "scm": "git",
        |  "has_wiki": false,
        |  "last_updated": "2016-01-26T21:39:24.485",
        |  "no_forks": false,
        |  "created_on": "2015-09-04T20:33:22.640",
        |  "owner": "carrots",
        |  "logo": "https://bitbucket.org/carrots/potatos/avatar/32/?ts=1453840764",
        |  "email_mailinglist": "",
        |  "is_mq": false,
        |  "size": 14544338,
        |  "read_only": false,
        |  "fork_of": null,
        |  "mq_of": null,
        |  "state": "available",
        |  "utc_created_on": "2015-09-04 18:37:22+00:00",
        |  "website": "",
        |  "description": "",
        |  "has_issues": false,
        |  "is_fork": false,
        |  "slug": "potatos",
        |  "is_private": true,
        |  "name": "Carrots and company",
        |  "language": "",
        |  "utc_last_updated": "2016-01-26 20:39:24+00:00",
        |  "no_public_forks": true,
        |  "creator": null,
        |  "resource_uri": "/1.0/repositories/carrots/potatos"
        |},
        |{
        |  "scm": "git",
        |  "has_wiki": false,
        |  "last_updated": "2017-02-03T20:29:18.224",
        |  "no_forks": false,
        |  "created_on": "2017-02-01T17:42:37.640",
        |  "owner": "carrots",
        |  "logo": "https://bitbucket.org/carrots/potatos/avatar/32/?ts=1486150158",
        |  "email_mailinglist": "",
        |  "is_mq": false,
        |  "size": 13489122,
        |  "read_only": false,
        |  "fork_of": null,
        |  "mq_of": null,
        |  "state": "available",
        |  "utc_created_on": "2017-02-01 16:41:37+00:00",
        |  "website": "",
        |  "description": "credentials.certainteed.com (Rackspace)",
        |  "has_issues": false,
        |  "is_fork": false,
        |  "slug": "potatos",
        |  "is_private": true,
        |  "name": "Nice potatos",
        |  "language": "php",
        |  "utc_last_updated": "2017-02-03 19:29:18+00:00",
        |  "no_public_forks": true,
        |  "creator": null,
        |  "resource_uri": "/1.0/repositories/carrots/potatos"
        |}
        |]
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[Seq[SimpleRepository]]

    value.fold(e =>
      fail(s"$e"),
      repo => repo.length shouldBe 2
    )
  }

  it should "successfully parse a JSON into an array of SimpleRepository that has no milliseconds on the created date" in {

    val input =
      """[
        |{
        |  "scm": "git",
        |  "has_wiki": false,
        |  "last_updated": "2016-01-26T21:39:24.485",
        |  "no_forks": false,
        |  "created_on": "2015-09-04T20:33:22",
        |  "owner": "carrots",
        |  "logo": "https://bitbucket.org/carrots/potatos/avatar/32/?ts=1453840764",
        |  "email_mailinglist": "",
        |  "is_mq": false,
        |  "size": 14544338,
        |  "read_only": false,
        |  "fork_of": null,
        |  "mq_of": null,
        |  "state": "available",
        |  "utc_created_on": "2015-09-04 18:37:22+00:00",
        |  "website": "",
        |  "description": "",
        |  "has_issues": false,
        |  "is_fork": false,
        |  "slug": "potatos",
        |  "is_private": true,
        |  "name": "Carrots and company",
        |  "language": "",
        |  "utc_last_updated": "2016-01-26 20:39:24+00:00",
        |  "no_public_forks": true,
        |  "creator": null,
        |  "resource_uri": "/1.0/repositories/carrots/potatos"
        |}
        |]
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[Seq[SimpleRepository]]

    value.fold(e =>
      fail(s"$e"),
      repo => repo.length shouldBe 1
    )
  }

}
