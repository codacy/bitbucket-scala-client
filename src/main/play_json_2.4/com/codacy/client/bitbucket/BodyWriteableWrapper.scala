package com.codacy.client.bitbucket

object BodyWriteableWrapper {
  type BodyWriteable[A] = play.api.http.Writeable[A]
}
