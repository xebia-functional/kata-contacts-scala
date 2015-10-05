name := """kata-contacts"""

scalaVersion := "2.11.6"
javacOptions ++= Seq("-target", "1.7", "-source", "1.7") // so we can build with java8

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "jcenter" at "http://jcenter.bintray.com"
)

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % "test",
  "org.mockito" % "mockito-all" % "1.10.19" % "test")