import sbt._

object Dependencies {

  // Generic
  lazy val jodaTime = "joda-time" % "joda-time" % "2.7"
  lazy val scalatest = "org.scalatest" %% "scalatest" % "2.2.6" % "test"

  // Play framework
  lazy val playWS = "com.typesafe.play" %% "play-ws" % "2.4.3"

}
