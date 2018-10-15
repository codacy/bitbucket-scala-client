package com.codacy.client.bitbucket.v1.util

object CommitHelper {
  def anchor(commitUUID: String): String = {
    commitUUID.take(12)
  }
}
