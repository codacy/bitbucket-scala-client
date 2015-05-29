import Dependencies._

name := """bitbucket-scala-client"""

version := "1.1"

scalaVersion := "2.10.5"

crossScalaVersions := Seq("2.10.5", "2.11.6")

scalacOptions := Seq("-deprecation", "-feature", "-unchecked", "-Ywarn-adapted-args", "-Xlint")

resolvers += "Typesafe maven repository" at "http://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Seq(
  jodaTime,
  playWS
)

organization := "com.codacy"

organizationName := "Codacy"

organizationHomepage := Some(new URL("https://www.codacy.com"))

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

startYear := Some(2014)

description := "Bitbucket Scala Client"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/codacy/bitbucket-scala-client.git"))

pomExtra :=
  <scm>
    <url>https://github.com/codacy/bitbucket-scala-client.git</url>
    <connection>scm:git:git@github.com:codacy/bitbucket-scala-client.git</connection>
    <developerConnection>scm:git:https://github.com/codacy/bitbucket-scala-client.git</developerConnection>
  </scm>
    <developers>
      <developer>
        <id>rtfpessoa</id>
        <name>Rodrigo</name>
        <email>rodrigo [at] codacy.com</email>
        <url>https://github.com/rtfpessoa</url>
      </developer>
    </developers>
