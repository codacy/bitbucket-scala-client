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

  it should "successfully parse a JSON into an array of TeamWithPermission" in {
    val rawJson = TestUtils.getTestContent("/user-service/user_permissions_teams.json")
    val json = Json.parse(rawJson)
    val value = json.validate[Seq[TeamWithPermission]]

    value.fold(e => fail(s"$e"), teams => teams.length shouldBe 2)
  }

  it should "successfully parse a JSON into a User" in {
    val rawJson = TestUtils.getTestContent("/user-service/user.json")
    val json = Json.parse(rawJson)
    val value = json.validate[User]

    value.fold(
      e => fail(s"$e"),
      user =>
        inside(user) {
          case User(account_id, uuid, display_name, nickname, avatarUrl) =>
            account_id shouldBe "123abc456def789ghi101jkl"
            uuid.value shouldBe "{c19f822b-0e29-433a-87a5-ec8ace58aa67}"
            display_name shouldBe "João Lopes"
            nickname shouldBe Some("jllopes")
            avatarUrl shouldBe Some("https://bitbucket.org/account/jllopes/avatar/")
      }
    )
  }

  it should "successfully parse a JSON into a UserPermission" in {
    val rawJson = TestUtils.getTestContent("/user-service/user_permissions.json")
    val json = Json.parse(rawJson)
    val value = json.validate[UserPermission]

    value.fold(
      e => fail(s"$e"),
      userPermission =>
        inside(userPermission) {
          case UserPermission(user, repository, permission) =>
            user shouldBe SimpleUser("{d301aafa-d676-4ee0-88be-962be7417567}", "Erik van Zijst")
            repository shouldBe SimpleRepository("{85d08b4e-571d-44e9-a507-fa476535aa98}", "geordi", "bitbucket/geordi")
            permission shouldBe "admin"
      }
    )
  }
}
