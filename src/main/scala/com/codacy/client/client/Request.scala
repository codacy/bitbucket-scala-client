package com.codacy.client.client

case class Request[T](url: String, classType: Class[T])
