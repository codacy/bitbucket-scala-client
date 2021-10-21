import sbt._

object Dependencies {
  val playVersion: String = "2.7.4"

  val playJson = Seq(
    "com.typesafe.play" %% "play-json-joda" % playVersion,
    "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.0.8",
    "com.typesafe.play" %% "play-ws-standalone-json" % "2.0.8"
  )

}
