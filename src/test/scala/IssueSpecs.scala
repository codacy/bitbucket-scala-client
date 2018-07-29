import com.codacy.client.bitbucket.Issue
import org.scalatest.{Matchers, _}
import play.api.libs.json.Json

class IssueSpecs extends FlatSpec with Matchers {

  "IssueSpecs" should "successfully parse Issue" in {
    val input =
      """
        |{
        |  "status": "new",
        |  "priority": "minor",
        |  "title": "ahoy!",
        |  "reported_by": {
        |    "username": "johannegger",
        |    "first_name": "Johann",
        |    "last_name": "Οικονόμου",
        |    "display_name": "Johann Οικονόμου",
        |    "is_staff": false,
        |    "avatar": "https://bitbucket.org/account/johannegger/avatar/32/?ts=1521969096",
        |    "resource_uri": "/1.0/users/johannegger",
        |    "is_team": false
        |  },
        |  "utc_last_updated": "2016-06-03 07:26:19+00:00",
        |  "responsible": {
        |    "username": "johannegger",
        |    "first_name": "Johann",
        |    "last_name": "Οικονόμου",
        |    "display_name": "Johann Οικονόμου",
        |    "is_staff": false,
        |    "avatar": "https://bitbucket.org/account/johannegger/avatar/32/?ts=1521969096",
        |    "resource_uri": "/1.0/users/johannegger",
        |    "is_team": false
        |  },
        |  "created_on": "2016-06-03T09:26:19.775",
        |  "metadata": {
        |    "kind": "enhancement",
        |    "version": null,
        |    "component": null,
        |    "milestone": null
        |  },
        |  "content": "Arrrr!",
        |  "comment_count": 0,
        |  "local_id": 9,
        |  "follower_count": 1,
        |  "utc_created_on": "2016-06-03 07:26:19+00:00",
        |  "resource_uri": "/1.0/repositories/example/test/issues/9",
        |  "is_spam": false
        |}
      """.stripMargin
    val json = Json.parse(input)
    val value = json.validate[Issue]

    value.fold(e =>
      fail(s"$e"),
      r => r.id shouldBe 9
    )
  }

}
