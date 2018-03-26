import sbt._

object Dependencies {

  val playWsStandaloneVersion = "2.0.0-M1"
  val playWS = "com.typesafe.play" %% "play-ahc-ws-standalone" % playWsStandaloneVersion
  val playWSjson = "com.typesafe.play" %% "play-ws-standalone-json" % playWsStandaloneVersion

  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"

}
