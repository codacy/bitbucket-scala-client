import codacy.libs._

name := """bitbucket-scala-client"""

val scala211 = "2.11.12"
val scala212 = "2.12.10"

scalaVersion := scala212
crossScalaVersions := Seq(scala211, scala212)

scalacOptions := Seq("-deprecation", "-feature", "-unchecked", "-Ywarn-adapted-args", "-Xlint")

resolvers += "Typesafe maven repository" at "http://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Dependencies.playWsJson(scalaVersion.value) ++ Seq(scalatest % Test)

organizationName := "Codacy"

organizationHomepage := Some(new URL("https://www.codacy.com"))

startYear := Some(2014)

description := "Bitbucket Scala Client"

homepage := Some(url("https://github.com/codacy/bitbucket-scala-client.git"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/codacy/bitbucket-scala-client.git"),
    "scm:git:git@github.com:codacy/bitbucket-scala-client.git"
  )
)

pgpPassphrase := Option(System.getenv("SONATYPE_GPG_PASSPHRASE"))
  .map(_.toCharArray)

publicMvnPublish

unmanagedSourceDirectories.in(Compile) += (Dependencies.playWsJson(scalaVersion.value).head.revision match {
  case Dependencies.playJson24 =>
    sourceDirectory.value / "main" / "play_json_2.4"
  case Dependencies.playJson27 =>
    sourceDirectory.value / "main" / "play_json_2.7"
  case _ =>
    throw new Exception("Unsupported Play JSON version")
})

name := (Dependencies.playWsJson(scalaVersion.value).head.revision match {
  case Dependencies.playJson24 => s"${name.value}_playjson24"
  case Dependencies.playJson27 => s"${name.value}_playjson27"
  case _ => throw new Exception("Unsupported Play JSON version")
})
