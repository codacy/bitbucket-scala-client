package com.codacy.client.bitbucket.client

case class RequestResponse[T](value: Option[T], message: String = "", hasError: Boolean = false)
