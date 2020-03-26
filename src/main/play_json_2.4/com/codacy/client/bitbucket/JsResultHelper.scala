package com.codacy.client.bitbucket

import play.api.data.validation.ValidationError

object JsResultHelper {
  def error(error: String): ValidationError = ValidationError(error)
}
