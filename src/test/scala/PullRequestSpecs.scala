import org.scalatest._
import org.scalatest.Matchers
import play.api.libs.json.Json
import com.codacy.client.bitbucket.v1.{Email, PullRequest}
import com.codacy.client.bitbucket.v1.PullRequest._

class PullRequestSpecs extends FlatSpec with Matchers {

  "PullRequestSpecs" should "successfully parse JSON with a commit" in {
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

  it should "successfully parse a JSON into an array of Email" in {
    val input =
      """[
        |{
        |  "active": true,
        |  "email": "2team.bb@gmail.com",
        |  "primary": true
        |},
        |{
        |  "active": false,
        |  "email": "ourteam@gmail.com",
        |  "primary": false
        |}
        |]
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[Seq[Email]]

    value.fold(e =>
      fail(s"$e"),
      emails => emails.length shouldBe 2
    )
  }
}
