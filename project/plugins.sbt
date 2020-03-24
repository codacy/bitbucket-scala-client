resolvers += Resolver.jcenterRepo

addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "0.2.0")

// Temporary until maven central sync, to be removed
resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("com.codacy" % "codacy-sbt-plugin" % "20.0.4")
