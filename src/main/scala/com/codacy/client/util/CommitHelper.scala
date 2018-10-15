package com.codacy.client.util

object CommitHelper {
  def anchor(commitUUID: String): String = {
    commitUUID.take(12)
  }
}
