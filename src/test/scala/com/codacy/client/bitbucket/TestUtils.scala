package com.codacy.client.bitbucket

import scala.io.Source

object TestUtils {

  /**
    * Helper function which will read the content of a file into a string. Used to test JSON responses.
    * @param path the relative path of the json file to consume
    */
  def getTestContent(path: String): String = {
    val file = Source.fromFile(getClass.getResource(path).getFile)

    try {
      file.mkString
    } finally {
      file.close()
    }
  }
}
