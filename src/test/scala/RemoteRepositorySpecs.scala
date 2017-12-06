import org.scalatest._
import org.scalatest.Matchers
import play.api.libs.json.Json
import com.codacy.client.bitbucket.{PullRequest, SimpleRepository}
import com.codacy.client.bitbucket.PullRequest._

class RemoteRepositorySpecs extends FlatSpec with Matchers {

  "RemoteRepository" should "successfully parse JSON with a commit" in {
    val input =
      """
        |{
        |      "description": "",
        |      "links": {
        |        "decline": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/decline"
        |        },
        |        "commits": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/commits"
        |        },
        |        "self": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368"
        |        },
        |        "comments": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/comments"
        |        },
        |        "merge": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/merge"
        |        },
        |        "html": {
        |          "href": "https://bitbucket.org//frontend./pull-requests/368"
        |        },
        |        "activity": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/activity"
        |        },
        |        "diff": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/diff"
        |        },
        |        "approve": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/approve"
        |        },
        |        "statuses": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/statuses"
        |        }
        |      },
        |      "title": "title",
        |      "close_source_branch": true,
        |      "merge_commit": null,
        |      "destination": {
        |        "commit": {
        |           "hash" : "random_hash"
        |        },
        |        "repository": {
        |          "links": {
        |            "self": {
        |              "href": "https://bitbucket.org/!api/2.0/repositories//frontend."
        |            },
        |            "html": {
        |              "href": "https://bitbucket.org//frontend."
        |            },
        |            "avatar": {
        |              "href": "https://bitbucket.org//frontend./avatar/32/"
        |            }
        |          },
        |          "type": "repository",
        |          "name": "frontend.",
        |          "full_name": "/frontend.",
        |          "uuid": "{     d8e5fe0b-fa32-47f1-a98b-88973c00c1d8}"
        |        },
        |        "branch": {
        |          "name": "FASDREI-2035"
        |        }
        |      },
        |      "state": "OPEN",
        |      "closed_by": null,
        |      "source": {
        |        "commit": {
        |          "hash": "ae0ff1c530b7",
        |          "links": {
        |            "self": {
        |              "href": "https://bitbucket.org/!api/2.0/repositories//frontend./commit/ae0ff1c530b7"
        |            }
        |          }
        |        },
        |        "repository": {
        |          "links": {
        |            "self": {
        |              "href": "https://bitbucket.org/!api/2.0/repositories//frontend."
        |            },
        |            "html": {
        |              "href": "https://bitbucket.org//frontend."
        |            },
        |            "avatar": {
        |              "href": "https://bitbucket.org//frontend./avatar/32/"
        |            }
        |          },
        |          "type": "repository",
        |          "name": "frontend.",
        |          "full_name": "/frontend.",
        |          "uuid": "{     d8e5fe0b-fa32-47f1-a98b-88973c00c1d8}"
        |        },
        |        "branch": {
        |          "name": "FASDREI-2033"
        |        }
        |      },
        |      "comment_count": 0,
        |      "author": {
        |        "username": "username",
        |        "display_name": "user name",
        |        "type": "user",
        |        "uuid": "{     f6f653be-75ea-4dc9-84af-645a5ede93aa}",
        |        "links": {
        |          "self": {
        |            "href": "https://bitbucket.org/!api/2.0/users/username"
        |          },
        |          "html": {
        |            "href": "https://bitbucket.org/username/"
        |          },
        |          "avatar": {
        |            "href": "https://bitbucket.org/account/username/avatar/32/"
        |          }
        |        }
        |      },
        |      "created_on": "2016-06-16T16:08:51.006182+00:00",
        |      "reason": "",
        |      "updated_on": "2016-09-08T17:27:22.050009+00:00",
        |      "type": "pullrequest",
        |      "id": 368,
        |      "task_count": 0
        |    }
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[PullRequest]

    value.fold(e =>
      fail(s"$e"),
      r => r.destCommit.isDefined shouldBe true
    )
  }

  it should "successfully parse JSON with null commit" in {
    val input =
      """
        |{
        |      "description": "",
        |      "links": {
        |        "decline": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/decline"
        |        },
        |        "commits": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/commits"
        |        },
        |        "self": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368"
        |        },
        |        "comments": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/comments"
        |        },
        |        "merge": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/merge"
        |        },
        |        "html": {
        |          "href": "https://bitbucket.org//frontend./pull-requests/368"
        |        },
        |        "activity": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/activity"
        |        },
        |        "diff": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/diff"
        |        },
        |        "approve": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/approve"
        |        },
        |        "statuses": {
        |          "href": "https://bitbucket.org/!api/2.0/repositories//frontend./pullrequests/368/statuses"
        |        }
        |      },
        |      "title": "title",
        |      "close_source_branch": true,
        |      "merge_commit": null,
        |      "destination": {
        |        "commit": null,
        |        "repository": {
        |          "links": {
        |            "self": {
        |              "href": "https://bitbucket.org/!api/2.0/repositories//frontend."
        |            },
        |            "html": {
        |              "href": "https://bitbucket.org//frontend."
        |            },
        |            "avatar": {
        |              "href": "https://bitbucket.org//frontend./avatar/32/"
        |            }
        |          },
        |          "type": "repository",
        |          "name": "frontend.",
        |          "full_name": "/frontend.",
        |          "uuid": "{     d8e5fe0b-fa32-47f1-a98b-88973c00c1d8}"
        |        },
        |        "branch": {
        |          "name": "FASDREI-2035"
        |        }
        |      },
        |      "state": "OPEN",
        |      "closed_by": null,
        |      "source": {
        |        "commit": {
        |          "hash": "ae0ff1c530b7",
        |          "links": {
        |            "self": {
        |              "href": "https://bitbucket.org/!api/2.0/repositories//frontend./commit/ae0ff1c530b7"
        |            }
        |          }
        |        },
        |        "repository": {
        |          "links": {
        |            "self": {
        |              "href": "https://bitbucket.org/!api/2.0/repositories//frontend."
        |            },
        |            "html": {
        |              "href": "https://bitbucket.org//frontend."
        |            },
        |            "avatar": {
        |              "href": "https://bitbucket.org//frontend./avatar/32/"
        |            }
        |          },
        |          "type": "repository",
        |          "name": "frontend.",
        |          "full_name": "/frontend.",
        |          "uuid": "{     d8e5fe0b-fa32-47f1-a98b-88973c00c1d8}"
        |        },
        |        "branch": {
        |          "name": "FASDREI-2033"
        |        }
        |      },
        |      "comment_count": 0,
        |      "author": {
        |        "username": "username",
        |        "display_name": "user name",
        |        "type": "user",
        |        "uuid": "{     f6f653be-75ea-4dc9-84af-645a5ede93aa}",
        |        "links": {
        |          "self": {
        |            "href": "https://bitbucket.org/!api/2.0/users/username"
        |          },
        |          "html": {
        |            "href": "https://bitbucket.org/username/"
        |          },
        |          "avatar": {
        |            "href": "https://bitbucket.org/account/username/avatar/32/"
        |          }
        |        }
        |      },
        |      "created_on": "2016-06-16T16:08:51.006182+00:00",
        |      "reason": "",
        |      "updated_on": "2016-09-08T17:27:22.050009+00:00",
        |      "type": "pullrequest",
        |      "id": 368,
        |      "task_count": 0
        |    }
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[PullRequest]

    value.fold(e =>
      fail(s"$e"),
      r => r.destCommit.isEmpty shouldBe true
    )
  }

  it should "successfully parse a JSON with null author while getting info for PRs" in {

    val input =
      """
        |{
        |  "description": "",
        |  "links": {
        |    "decline": {
        |      "href": "https://bitbucket.org/wtv"
        |    },
        |    "commits": {
        |      "href": "https://bitbucket.org/wtv"
        |    },
        |    "self": {
        |      "href": "https://bitbucket.org/wtv"
        |    },
        |    "comments": {
        |      "href": "https://bitbucket.org/wtv"
        |    },
        |    "merge": {
        |      "href": "https://bitbucket.org/wtv"
        |    },
        |    "html": {
        |      "href": "https://bitbucket.org/wtv"
        |    },
        |    "activity": {
        |      "href": "https://bitbucket.org/wtv"
        |    },
        |    "diff": {
        |      "href": "https://bitbucket.org/wtv"
        |    },
        |    "approve": {
        |      "href": "https://bitbucket.org/wtv"
        |    },
        |    "statuses": {
        |      "href": "https://bitbucket.org/wtv"
        |    }
        |  },
        |  "title": "Potatos",
        |  "close_source_branch": true,
        |  "merge_commit": null,
        |  "destination": {
        |    "commit": {
        |      "hash": "aaaaaaaaaaaa",
        |      "links": {
        |        "self": {
        |          "href": "https://bitbucket.org/wtv"
        |        }
        |      }
        |    },
        |    "repository": {
        |      "links": {
        |        "self": {
        |          "href": "https://bitbucket.org/wtv"
        |        },
        |        "html": {
        |          "href": "https://bitbucket.org/wtv"
        |        },
        |        "avatar": {
        |          "href": "https://bitbucket.org/wtv"
        |        }
        |      },
        |      "type": "repository",
        |      "name": "my-potatos",
        |      "full_name": "codacy/my-potatos",
        |      "uuid": "{70bb8ec5-0c14-4964-aaaa-ebb4b2235ecf}"
        |    },
        |    "branch": {
        |      "name": "master"
        |    }
        |  },
        |  "state": "OPEN",
        |  "closed_by": null,
        |  "source": {
        |    "commit": {
        |      "hash": "bbbbbbbbbbbb",
        |      "links": {
        |        "self": {
        |          "href": "https://bitbucket.org/wtv"
        |        }
        |      }
        |    },
        |    "repository": {
        |      "links": {
        |        "self": {
        |          "href": "https://bitbucket.org/wtv"
        |        },
        |        "html": {
        |          "href": "https://bitbucket.org/wtv"
        |        },
        |        "avatar": {
        |          "href": "https://bitbucket.org/wtv"
        |        }
        |      },
        |      "type": "repository",
        |      "name": "my-potatos",
        |      "full_name": "codacy/my-potatos",
        |      "uuid": "{c08af10a-b49f-4a7a-9edb-6a66b3495a6f}"
        |    },
        |    "branch": {
        |      "name": "add-more-potatos"
        |    }
        |  },
        |  "comment_count": 0,
        |  "author": null,
        |  "created_on": "2016-04-05T14:35:41.678131+00:00",
        |  "reason": "",
        |  "updated_on": "2016-11-01T14:25:45.932586+00:00",
        |  "type": "pullrequest",
        |  "id": 333,
        |  "task_count": 0
        |}
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[PullRequest]

    value.fold(e =>
      fail(s"$e"),
      r => r.destCommit.isDefined shouldBe true
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
}
