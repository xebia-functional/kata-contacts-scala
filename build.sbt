name := """kata-contacts"""

scalaVersion := "2.11.7"
javacOptions ++= Seq("-target", "1.7", "-source", "1.7") // so we can build with java8

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "jcenter" at "http://jcenter.bintray.com"
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.4",
  "org.specs2" %% "specs2-core" % "3.6.4" % "test",
  "org.specs2" %% "specs2-mock" % "3.6.4" % "test")