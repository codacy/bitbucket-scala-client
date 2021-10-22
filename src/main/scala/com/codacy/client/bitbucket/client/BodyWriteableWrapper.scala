package com.codacy.client.bitbucket.client

object BodyWriteableWrapper {
  type BodyWriteable[A] = play.api.libs.ws.BodyWritable[A]
}
