import sbt._

object Dependencies {

  def playJson(playJsonVersion: String) = {
    val playwsVersion =
      if (playJsonVersion.startsWith("2.8.")) "2.1.11"
      else if (playJsonVersion.startsWith("2.7.")) "2.0.8"
      else sys.error("Missing play-ws version. Check the compatible version based on its pom")
    Seq(
      "com.typesafe.play" %% "play-json-joda" % playJsonVersion,
      "com.typesafe.play" %% "play-ahc-ws-standalone" % playwsVersion,
      "com.typesafe.play" %% "play-ws-standalone-json" % playwsVersion
    )
  }

}
