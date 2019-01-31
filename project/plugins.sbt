import sbt.Keys.resolvers
import sbt._

resolvers := Seq(Resolver.bintrayIvyRepo("typesafe", "ivy-releases"),
  Resolver.bintrayRepo("typesafe", "maven-releases"),
  Resolver.bintrayRepo("sbt", "maven-releases")) ++ resolvers.value

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "2.3")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")

addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "0.2.0")
