package com.codacy.client.bitbucket.v2
import com.codacy.client.bitbucket.TestUtils
import org.scalatest.{Matchers, _}
import play.api.libs.json.{JsSuccess, Json}

class RepositorySpecs extends FlatSpec with Matchers {

  "RepositorySpecs" should "successfully parse DeployKey" in {
    val input =
      """
        |{
        |    "id": 123,
        |    "key": "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDAK/b1cHHDr/TEV1JGQl+WjCwStKG6Bhrv0rFpEsYlyTBm1fzN0VOJJYn4ZOPCPJwqse6fGbXntEs+BbXiptR+++HycVgl65TMR0b5ul5AgwrVdZdT7qjCOCgaSV74/9xlHDK8oqgGnfA7ZoBBU+qpVyaloSjBdJfLtPY/xqj4yHnXKYzrtn/uFc4Kp9Tb7PUg9Io3qohSTGJGVHnsVblq/rToJG7L5xIo0OxK0SJSQ5vuId93ZuFZrCNMXj8JDHZeSEtjJzpRCBEXHxpOPhAcbm4MzULgkFHhAVgp4JbkrT99/wpvZ7r9AdkTg7HGqL3rlaDrEcWfL7Lu6TnhBdq5",
        |    "label": "mydeploykey",
        |    "type": "deploy_key",
        |    "created_on": "2018-08-15T23:50:59.993890+00:00",
        |    "repository": {
        |        "full_name": "mleu/test",
        |        "name": "test",
        |        "type": "repository",
        |        "uuid": "{85d08b4e-571d-44e9-a507-fa476535aa98}"
        |    },
        |    "links":{
        |        "self":{
        |            "href": "https://api.bitbucket.org/2.0/repositories/mleu/test/deploy-keys/123"
        |        }
        |    },
        |    "last_used": null,
        |    "comment": "mleu@C02W454JHTD8"
        |}
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[DeployKey]

    value.fold(e => fail(s"$e"), r => r.id shouldBe 123)
  }

  it should "successfully parse Repository" in {
    val input =
      """{
                  |    "scm": "hg",
                  |    "website": "",
                  |    "has_wiki": true,
                  |    "name": "tweakmsg",
                  |    "links": {
                  |        "watchers": {
                  |            "href": "https://api.bitbucket.org/2.0/repositories/phlogistonjohn/tweakmsg/watchers"
                  |        },
                  |        "branches": {
                  |            "href": "https://api.bitbucket.org/2.0/repositories/phlogistonjohn/tweakmsg/refs/branches"
                  |        },
                  |        "tags": {
                  |            "href": "https://api.bitbucket.org/2.0/repositories/phlogistonjohn/tweakmsg/refs/tags"
                  |        },
                  |        "commits": {
                  |            "href": "https://api.bitbucket.org/2.0/repositories/phlogistonjohn/tweakmsg/commits"
                  |        },
                  |        "clone": [
                  |            {
                  |                "href": "https://bitbucket.org/phlogistonjohn/tweakmsg",
                  |                "name": "https"
                  |            },
                  |            {
                  |                "href": "ssh://hg@bitbucket.org/phlogistonjohn/tweakmsg",
                  |                "name": "ssh"
                  |            }
                  |        ],
                  |        "self": {
                  |            "href": "https://api.bitbucket.org/2.0/repositories/phlogistonjohn/tweakmsg"
                  |        },
                  |        "source": {
                  |            "href": "https://api.bitbucket.org/2.0/repositories/phlogistonjohn/tweakmsg/src"
                  |        },
                  |        "html": {
                  |            "href": "https://bitbucket.org/phlogistonjohn/tweakmsg"
                  |        },
                  |        "avatar": {
                  |            "href": "https://bytebucket.org/ravatar/%7B59299fb9-3695-4e0c-b8ca-836888b83315%7D?ts=default"
                  |        },
                  |        "hooks": {
                  |            "href": "https://api.bitbucket.org/2.0/repositories/phlogistonjohn/tweakmsg/hooks"
                  |        },
                  |        "forks": {
                  |            "href": "https://api.bitbucket.org/2.0/repositories/phlogistonjohn/tweakmsg/forks"
                  |        },
                  |        "downloads": {
                  |            "href": "https://api.bitbucket.org/2.0/repositories/phlogistonjohn/tweakmsg/downloads"
                  |        },
                  |        "issues": {
                  |            "href": "https://api.bitbucket.org/2.0/repositories/phlogistonjohn/tweakmsg/issues"
                  |        },
                  |        "pullrequests": {
                  |            "href": "https://api.bitbucket.org/2.0/repositories/phlogistonjohn/tweakmsg/pullrequests"
                  |        }
                  |    },
                  |    "fork_policy": "allow_forks",
                  |    "uuid": "{59299fb9-3695-4e0c-b8ca-836888b83315}",
                  |    "language": "",
                  |    "created_on": "2008-06-25T00:53:00.273366+00:00",
                  |    "mainbranch": {
                  |        "type": "named_branch",
                  |        "name": "default"
                  |    },
                  |    "full_name": "phlogistonjohn/tweakmsg",
                  |    "has_issues": true,
                  |    "owner": {
                  |        "username": "phlogistonjohn",
                  |        "display_name": "John Mulligan",
                  |        "account_id": "557058:8ffe6a8c-8424-4156-9786-0102572cf345",
                  |        "links": {
                  |            "self": {
                  |                "href": "https://api.bitbucket.org/2.0/users/phlogistonjohn"
                  |            },
                  |            "html": {
                  |                "href": "https://bitbucket.org/phlogistonjohn/"
                  |            },
                  |            "avatar": {
                  |                "href": "https://bitbucket.org/account/phlogistonjohn/avatar/"
                  |            }
                  |        },
                  |        "nickname": "phlogistonjohn",
                  |        "type": "user",
                  |        "uuid": "{c8614bfa-831a-49eb-866b-4bdd87c8c2c2}"
                  |    },
                  |    "updated_on": "2012-06-24T17:32:27.458855+00:00",
                  |    "size": 7085,
                  |    "type": "repository",
                  |    "slug": "tweakmsg",
                  |    "is_private": false,
                  |    "project": {"name": "tweakmsg", "key": "TWEAKMSG"},
                  |    "description": "Mercurial (hg) extension to allow commenting on commit messages.  Mainly written for practice reading & working with mercurial internals.\r\n"
                  |}""".stripMargin
    val json = Json.parse(input)
    val value = json.validate[Repository]

    value.fold(e => fail(s"$e"), r => {
      r.name shouldBe "tweakmsg"
      r.owner.display_name shouldBe "John Mulligan"
      r.project.map(_.name) shouldBe Some("tweakmsg")
    })
  }

  it should "parse minimal repository without optional fields" in {
    val rawJson = TestUtils.getTestContent("/test-files/minimal_repository.json")
    val value = Json.parse(rawJson).validate[Repository]
    value shouldBe a[JsSuccess[_]]
  }
}
