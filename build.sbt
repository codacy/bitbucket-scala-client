import Dependencies._

name := """bitbucket-scala-client"""

version := "1.0"

scalaVersion := "2.11.1"

resolvers += "Typesafe maven repository" at "http://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Seq(
  jodaTime,
  playWS
)
