import codacy.libs._

name := """bitbucket-scala-client"""

val scala212 = "2.12.10"

scalaVersion := scala212

scalacOptions := Seq("-deprecation", "-feature", "-unchecked", "-Ywarn-adapted-args", "-Xlint")

resolvers +=
  "Typesafe maven repository" at "https://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Dependencies.playJson ++ Seq(scalatest % Test)

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

name := s"${name.value}_playjson${Dependencies.playVersion.split('.').take(2).mkString}"
