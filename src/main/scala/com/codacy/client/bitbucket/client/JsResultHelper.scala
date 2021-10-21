package com.codacy.client.bitbucket.client

import play.api.libs.json.JsonValidationError

object JsResultHelper {
  def error(error: String): JsonValidationError = JsonValidationError(error)
}
