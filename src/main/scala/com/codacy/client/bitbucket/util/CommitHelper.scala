package com.codacy.client.bitbucket.util

object CommitHelper {
  def anchor(commitUUID: String): String = {
    commitUUID.take(12)
  }
}
