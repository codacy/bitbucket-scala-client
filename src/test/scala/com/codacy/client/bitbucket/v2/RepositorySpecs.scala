package com.codacy.client.bitbucket.v2
import org.scalatest.{Matchers, _}
import play.api.libs.json.Json

class RepositorySpecs extends FlatSpec with Matchers {

  "RepositorySpecs" should "successfully parse DeployKey" in {
    val input = """
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
}
