package com.codacy.client.bitbucket.v2
import com.codacy.client.bitbucket.TestUtils
import org.scalatest.OptionValues._
import org.scalatest.{Matchers, _}
import play.api.libs.json.Json

class UserSpecs extends FlatSpec with Matchers with Inside {

  "UserSpecs" should "successfully parse a JSON into an array of Email" in {
    val rawJson = TestUtils.getTestContent("/user-service/user_emails.json")
    val json = Json.parse(rawJson)
    val value = json.validate[Seq[Email]]

    value.fold(e => fail(s"$e"), emails => emails.length shouldBe 3)
  }

  it should "successfully parse a JSON into a User" in {
    val rawJson = TestUtils.getTestContent("/user-service/user.json")
    val json = Json.parse(rawJson)
    val value = json.validate[User]

    value.fold(
      e => fail(s"$e"),
      user =>
        inside(user) {
          case User(uuid, display_name, nickname, avatarUrl) =>
            uuid shouldBe "{c19f822b-0e29-433a-87a5-ec8ace58aa67}"
            display_name shouldBe "João Lopes"
            nickname shouldBe Some("jllopes")
            avatarUrl shouldBe Some("https://bitbucket.org/account/jllopes/avatar/")
      }
    )
  }

  it should "successfully parse a JSON into a UserIdentifiers " in {
    val rawJson = TestUtils.getTestContent("/user-service/userIdentifiers.json")
    val json = Json.parse(rawJson)
    val value = json.validate[UserIdentifiers]

    value.fold(
      e => fail(s"$e"),
      userId =>
        inside(userId) {
          case UserIdentifiers(account_id, uuid) =>
            account_id shouldBe "557058:b648273b-dcd8-4c95-be5c-484cf165069d"
            uuid shouldBe Some("{899b778f-d7f5-4664-bbf7-5a6f88b0728f}")
      }
    )
  }
}
