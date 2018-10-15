package com.codacy.client.bitbucket.v1.util

object HTTPStatusCodes extends Enumeration {
  type HTTPStatusCodes = Value

  val OK = 200
  val CREATED = 201
  val NO_CONTENT = 204
  val BAD_REQUEST = 400
  val UNAUTHORIZED = 401
  val FORBIDDEN = 403
  val NOT_FOUND = 404
}
