import sbt._

object Dependencies {

  val playWsStandaloneVersion = "2.0.0-M1"
  val playWS = "com.typesafe.play" %% "play-ahc-ws-standalone" % playWsStandaloneVersion
  val playWSjson = "com.typesafe.play" %% "play-ws-standalone-json" % playWsStandaloneVersion
  val playJson_211 = "com.typesafe.play" %% "play-json" % "2.4.3"

  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"

  def playJson(scalaVersion: String): ModuleID = withScalaVersion(scalaVersion)(
    playWSjson,
    playJson_211
  )

  private def withScalaVersion(scalaVersion: String)(
      scala212: ModuleID,
      scalaFallback: ModuleID): ModuleID = {
    if (scalaVersion.startsWith("2.12")) scala212 else scalaFallback
  }

}
