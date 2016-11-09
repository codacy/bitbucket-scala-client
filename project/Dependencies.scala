import sbt._

object Dependencies {

  // Generic
  lazy val jodaTime = "joda-time" % "joda-time" % "2.7"

  // Play framework
  lazy val playWS = "com.typesafe.play" %% "play-ws" % "2.4.3"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "2.2.6" % "test"
}
