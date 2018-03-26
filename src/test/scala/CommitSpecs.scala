import com.codacy.client.bitbucket.{Commit, CommitComment, SimpleCommit}
import org.scalatest.{Matchers, _}
import play.api.libs.json.Json

class CommitSpecs extends FlatSpec with Matchers {

  "CommitSpecs" should "successfully parse Commit" in {
    val input =
      """
        |{
        |  "hash": "014d431153a45e14afec424ecfe91005cf4d44b2",
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
        |    "uuid": "{59b1e62a-a277-4c1d-8b05-6059289a3e67}"
        |  },
        |  "links": {
        |    "self": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/014d431153a45e14afec424ecfe91005cf4d44b2"
        |    },
        |    "comments": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/014d431153a45e14afec424ecfe91005cf4d44b2/comments"
        |    },
        |    "patch": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/patch/014d431153a45e14afec424ecfe91005cf4d44b2"
        |    },
        |    "html": {
        |      "href": "https://bitbucket.org/example/test/commits/014d431153a45e14afec424ecfe91005cf4d44b2"
        |    },
        |    "diff": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/diff/014d431153a45e14afec424ecfe91005cf4d44b2"
        |    },
        |    "approve": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/014d431153a45e14afec424ecfe91005cf4d44b2/approve"
        |    },
        |    "statuses": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/014d431153a45e14afec424ecfe91005cf4d44b2/statuses"
        |    }
        |  },
        |  "author": {
        |    "raw": "Rafael Cortês <mrfyda@gmail.com>",
        |    "user": {
        |      "username": "mrfyda",
        |      "display_name": "Rafael Cortês",
        |      "type": "user",
        |      "uuid": "{1e960327-38ba-4100-919e-5a677e668cad}",
        |      "links": {
        |        "self": {
        |          "href": "https://bitbucket.org/!api/2.0/users/mrfyda"
        |        },
        |        "html": {
        |          "href": "https://bitbucket.org/mrfyda/"
        |        },
        |        "avatar": {
        |          "href": "https://bitbucket.org/account/mrfyda/avatar/32/"
        |        }
        |      }
        |    }
        |  },
        |  "summary": {
        |    "raw": "ahoy!\n",
        |    "markup": "markdown",
        |    "html": "<p>ahoy!</p>",
        |    "type": "rendered"
        |  },
        |  "participants": [],
        |  "parents": [{
        |    "hash": "b7c4f26e3a23f871452de3a4f772afb11db7530e",
        |    "type": "commit",
        |    "links": {
        |      "self": {
        |        "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/b7c4f26e3a23f871452de3a4f772afb11db7530e"
        |      },
        |      "html": {
        |        "href": "https://bitbucket.org/example/test/commits/b7c4f26e3a23f871452de3a4f772afb11db7530e"
        |      }
        |    }
        |  }],
        |  "date": "2018-03-23T18:56:44+00:00",
        |  "message": "ahoy!\n",
        |  "type": "commit"
        |}
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[Commit]

    value.fold(e =>
      fail(s"$e"),
      r => r.hash shouldBe "014d431153a45e14afec424ecfe91005cf4d44b2"
    )
  }

  it should "successfully parse SimpleCommit" in {
    val input =
      """
        |{
        |  "hash": "62d8d2d067452c458c91b2e932eb70d830ad2e29",
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
        |    "name": "test",
        |    "full_name": "example/test",
        |    "uuid": "{c08af10a-b49f-4a7a-9edb-6a66b3495a6f}"
        |  },
        |  "links": {
        |    "self": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/62d8d2d067452c458c91b2e932eb70d830ad2e29"
        |    },
        |    "comments": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/62d8d2d067452c458c91b2e932eb70d830ad2e29/comments"
        |    },
        |    "patch": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/patch/62d8d2d067452c458c91b2e932eb70d830ad2e29"
        |    },
        |    "html": {
        |      "href": "https://bitbucket.org/example/test/commits/62d8d2d067452c458c91b2e932eb70d830ad2e29"
        |    },
        |    "diff": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/diff/62d8d2d067452c458c91b2e932eb70d830ad2e29"
        |    },
        |    "approve": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/62d8d2d067452c458c91b2e932eb70d830ad2e29/approve"
        |    },
        |    "statuses": {
        |      "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/62d8d2d067452c458c91b2e932eb70d830ad2e29/statuses"
        |    }
        |  },
        |  "author": {
        |    "raw": "Pedro Amaral <pamaral@codacy.com>",
        |    "type": "author",
        |    "user": {
        |      "username": "pedromcamaral",
        |      "display_name": "Pedro Amaral",
        |      "type": "user",
        |      "uuid": "{4b294dbe-ccfd-447d-96e9-f265923008d3}",
        |      "links": {
        |        "self": {
        |          "href": "https://bitbucket.org/!api/2.0/users/pedromcamaral"
        |        },
        |        "html": {
        |          "href": "https://bitbucket.org/pedromcamaral/"
        |        },
        |        "avatar": {
        |          "href": "https://bitbucket.org/account/pedromcamaral/avatar/32/"
        |        }
        |      }
        |    }
        |  },
        |  "parents": [{
        |    "hash": "9f2538232c9fca196b992b5be0eabbfa3179023b",
        |    "type": "commit",
        |    "links": {
        |      "self": {
        |        "href": "https://bitbucket.org/!api/2.0/repositories/example/test/commit/9f2538232c9fca196b992b5be0eabbfa3179023b"
        |      },
        |      "html": {
        |        "href": "https://bitbucket.org/example/test/commits/9f2538232c9fca196b992b5be0eabbfa3179023b"
        |      }
        |    }
        |  }],
        |  "date": "2018-03-23T17:44:02+00:00",
        |  "message": "Land ho!\n",
        |  "type": "commit"
        |}
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[SimpleCommit]

    value.fold(e =>
      fail(s"$e"),
      r => r.hash shouldBe "62d8d2d067452c458c91b2e932eb70d830ad2e29"
    )
  }

}
