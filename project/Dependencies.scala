import sbt._

object Dependencies {
  val playJson24: String = "2.4.3"
  val playJson27: String = "2.7.4"

  def playWsJson(scalaVersion: String): Seq[ModuleID] = {
    val playJsonVersion = CrossVersion.partialVersion(scalaVersion) match {
      case _ if sys.props.contains("playVersion") => sys.props.get("playVersion").get
      case Some((2, 11)) => playJson24
      case Some((2, 12)) => playJson27
    }
    playJsonVersion match {
      case `playJson24` =>
        Seq("com.typesafe.play" %% "play-ws" % playJsonVersion, "com.typesafe.play" %% "play-json" % playJsonVersion)
      case jsonVersion =>
        Seq(
          "com.typesafe.play" %% "play-ws" % jsonVersion,
          "com.typesafe.play" %% "play-json" % jsonVersion,
          "com.typesafe.play" %% "play-json-joda" % jsonVersion
        )
    }
  }
}
