package com.codacy.client.bitbucket.client

case class Request[T](url: String, classType: Class[T])
