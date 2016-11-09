import org.scalatest._
import org.scalatest.Matchers
import play.api.libs.json.Json
import com.codacy.client.bitbucket.PullRequest
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

    val parsed = value.fold(e => false, r => r.destCommit.isDefined)

    parsed shouldBe true
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

    val parsed = value.fold(e =>false, r => r.destCommit.isEmpty)

    parsed shouldBe true
  }
}
