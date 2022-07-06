package com.codacy.client.bitbucket.v2

import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.Json
import org.scalatest.OptionValues._

class PullRequestSpecs extends FlatSpec with Matchers {
  "PullRequestSpecs" should "successfully parse  PullRequest" in {
    val input =
      """
        |{
        | "description":"README.md edited online with Bitbucket",
        | "links":
        | {
        |   "decline":
        |   {
        |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/decline"
        |   },
        |   "commits":
        |   {
        |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/commits"
        |   },
        |   "self":
        |   {
        |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3"
        |   },
        |   "comments":
        |   {
        |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/comments"
        |   },
        |   "merge":
        |   {
        |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/merge"
        |   },
        |   "html":
        |   {
        |     "href":"https:\/\/bitbucket.org\/jllopes\/test2\/pull-requests\/3"
        |   },
        |   "activity":
        |   {
        |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/activity"
        |   },
        |   "diff":
        |   {
        |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/diff"
        |   },
        |   "approve":
        |   {
        |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/approve"
        |   },
        |   "statuses":
        |   {
        |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/statuses"
        |   }
        | },
        | "title":"README.md edited online with Bitbucket",
        | "close_source_branch":true,
        | "merge_commit":null,
        | "destination":
        | {
        |   "commit":
        |   {
        |     "type":"commit",
        |     "hash":"a1dbc5a0d643",
        |     "links":
        |     {
        |       "self":
        |       {
        |         "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/commit\/a1dbc5a0d643"
        |       },
        |       "html":
        |       {
        |         "href":"https:\/\/bitbucket.org\/jllopes\/test2\/commits\/a1dbc5a0d643"
        |       }
        |     }
        |   },
        |   "branch":
        |   {
        |     "name":"jllopes\/asdf2"
        |   },
        |   "repository":
        |   {
        |     "full_name":"jllopes\/test2",
        |     "type":"repository",
        |     "name":"test2",
        |     "links":
        |     {
        |       "self":
        |       {
        |         "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2"
        |       },
        |       "html":
        |       {
        |         "href":"https:\/\/bitbucket.org\/jllopes\/test2"
        |       },
        |       "avatar":
        |       {
        |         "href":"https:\/\/bytebucket.org\/ravatar\/%7B2eaf162b-90f7-4a63-b42f-a53381685ceb%7D?ts=default"
        |       }
        |     },
        |     "uuid":"{2eaf162b-90f7-4a63-b42f-a53381685ceb}"
        |   }
        | },
        | "comment_count":0,
        | "closed_by":null,
        | "source":
        | {
        |   "commit":
        |   {
        |     "type":"commit",
        |     "hash":"096bc35a6ba0",
        |     "links":
        |     {
        |       "self":
        |       {
        |       "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/commit\/096bc35a6ba0"
        |       },
        |       "html":
        |       {
        |         "href":"https:\/\/bitbucket.org\/jllopes\/test2\/commits\/096bc35a6ba0"
        |       }
        |     }
        | },
        | "branch":
        | {
        |   "name":"asdf5"
        | },
        | "repository":
        |   {
        |     "full_name":"jllopes\/test2",
        |     "type":"repository",
        |     "name":"test2",
        |     "links":
        |     {
        |       "self":
        |       {
        |         "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2"
        |       },
        |       "html":
        |       {
        |         "href":"https:\/\/bitbucket.org\/jllopes\/test2"
        |       },
        |       "avatar":
        |       {
        |         "href":"https:\/\/bytebucket.org\/ravatar\/%7B2eaf162b-90f7-4a63-b42f-a53381685ceb%7D?ts=default"
        |       }
        |     },
        |     "uuid":"{2eaf162b-90f7-4a63-b42f-a53381685ceb}"
        |   }
        | },
        | "created_on":"2019-01-15T15:14:16.752685+00:00",
        | "state":"OPEN",
        | "task_count":0,
        | "reason":"",
        | "updated_on":"2019-01-31T11:28:53.675721+00:00",
        | "author":
        | {
        |   "username":"jllopes",
        |   "website":"",
        |   "display_name":"João Lopes",
        |   "links":
        |   {
        |     "hooks":
        |     {
        |       "href":"https:\/\/bitbucket.org\/!api\/2.0\/users\/jllopes\/hooks"
        |     },
        |     "self":
        |     {
        |       "href":"https:\/\/bitbucket.org\/!api\/2.0\/users\/jllopes"
        |     },
        |     "repositories":
        |     {
        |       "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes"
        |     },
        |     "html":
        |     {
        |       "href":"https:\/\/bitbucket.org\/jllopes\/"
        |     },
        |     "followers":
        |     {
        |       "href":"https:\/\/bitbucket.org\/!api\/2.0\/users\/jllopes\/followers"
        |     },
        |     "avatar":
        |     {
        |       "href":"https:\/\/bitbucket.org\/account\/jllopes\/avatar\/"
        |     },
        |     "following":
        |     {
        |       "href":"https:\/\/bitbucket.org\/!api\/2.0\/users\/jllopes\/following"
        |     },
        |     "snippets":
        |     {
        |       "href":"https:\/\/bitbucket.org\/!api\/2.0\/snippets\/jllopes"
        |     }
        |   },
        |   "type":"user",
        |   "created_on":"2018-07-02T10:41:55.342788+00:00",
        |   "is_staff":false,
        |   "location":null,
        |   "account_status":"active",
        |   "nickname":"jllopes",
        |   "uuid":"{c19f822b-0e29-433a-87a5-ec8ace58aa67}"
        | },
        | "summary":
        | {
        |   "raw":"README.md edited online with Bitbucket",
        |   "markup":"markdown",
        |   "html":"<p>README.md edited online with Bitbucket<\/p>",
        |   "type":"rendered"
        | },
        | "type":"pullrequest",
        | "id":3
        |}""".stripMargin

    val json = Json.parse(input)
    val value = json.validate[PullRequest]

    value.fold(e => fail(s"Failed parsing pull request json: $e"), pr => {
      pr.id shouldBe 3
      pr.author.value.uuid shouldBe "{c19f822b-0e29-433a-87a5-ec8ace58aa67}"
    })
  }

  it should "not fail when author is null while parsing  PullRequest" in {
    val input =
      """
          |{
          | "description":"README.md edited online with Bitbucket",
          | "links":
          | {
          |   "decline":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/decline"
          |   },
          |   "commits":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/commits"
          |   },
          |   "self":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3"
          |   },
          |   "comments":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/comments"
          |   },
          |   "merge":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/merge"
          |   },
          |   "html":
          |   {
          |     "href":"https:\/\/bitbucket.org\/jllopes\/test2\/pull-requests\/3"
          |   },
          |   "activity":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/activity"
          |   },
          |   "diff":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/diff"
          |   },
          |   "approve":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/approve"
          |   },
          |   "statuses":
          |   {
          |     "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/pullrequests\/3\/statuses"
          |   }
          | },
          | "title":"README.md edited online with Bitbucket",
          | "close_source_branch":true,
          | "merge_commit":null,
          | "destination":
          | {
          |   "commit":
          |   {
          |     "type":"commit",
          |     "hash":"a1dbc5a0d643",
          |     "links":
          |     {
          |       "self":
          |       {
          |         "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/commit\/a1dbc5a0d643"
          |       },
          |       "html":
          |       {
          |         "href":"https:\/\/bitbucket.org\/jllopes\/test2\/commits\/a1dbc5a0d643"
          |       }
          |     }
          |   },
          |   "branch":
          |   {
          |     "name":"jllopes\/asdf2"
          |   },
          |   "repository":
          |   {
          |     "full_name":"jllopes\/test2",
          |     "type":"repository",
          |     "name":"test2",
          |     "links":
          |     {
          |       "self":
          |       {
          |         "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2"
          |       },
          |       "html":
          |       {
          |         "href":"https:\/\/bitbucket.org\/jllopes\/test2"
          |       },
          |       "avatar":
          |       {
          |         "href":"https:\/\/bytebucket.org\/ravatar\/%7B2eaf162b-90f7-4a63-b42f-a53381685ceb%7D?ts=default"
          |       }
          |     },
          |     "uuid":"{2eaf162b-90f7-4a63-b42f-a53381685ceb}"
          |   }
          | },
          | "comment_count":0,
          | "closed_by":null,
          | "source":
          | {
          |   "commit":
          |   {
          |     "type":"commit",
          |     "hash":"096bc35a6ba0",
          |     "links":
          |     {
          |       "self":
          |       {
          |       "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2\/commit\/096bc35a6ba0"
          |       },
          |       "html":
          |       {
          |         "href":"https:\/\/bitbucket.org\/jllopes\/test2\/commits\/096bc35a6ba0"
          |       }
          |     }
          | },
          | "branch":
          | {
          |   "name":"asdf5"
          | },
          | "repository":
          |   {
          |     "full_name":"jllopes\/test2",
          |     "type":"repository",
          |     "name":"test2",
          |     "links":
          |     {
          |       "self":
          |       {
          |         "href":"https:\/\/bitbucket.org\/!api\/2.0\/repositories\/jllopes\/test2"
          |       },
          |       "html":
          |       {
          |         "href":"https:\/\/bitbucket.org\/jllopes\/test2"
          |       },
          |       "avatar":
          |       {
          |         "href":"https:\/\/bytebucket.org\/ravatar\/%7B2eaf162b-90f7-4a63-b42f-a53381685ceb%7D?ts=default"
          |       }
          |     },
          |     "uuid":"{2eaf162b-90f7-4a63-b42f-a53381685ceb}"
          |   }
          | },
          | "created_on":"2019-01-15T15:14:16.752685+00:00",
          | "state":"OPEN",
          | "task_count":0,
          | "reason":"",
          | "updated_on":"2019-01-31T11:28:53.675721+00:00",
          | "summary":
          | {
          |   "raw":"README.md edited online with Bitbucket",
          |   "markup":"markdown",
          |   "html":"<p>README.md edited online with Bitbucket<\/p>",
          |   "type":"rendered"
          | },
          | "type":"pullrequest",
          | "id":3
          |}""".stripMargin

    val json = Json.parse(input)
    val value = json.validate[PullRequest]

    value.fold(e => fail(s"Failed parsing pull request json: $e"), pr => {
      pr.id shouldBe 3
      pr.author shouldBe None
    })

  }

}
