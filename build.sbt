name := """reactive"""

version := "1.0"

scalaVersion := "2.11.1"

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.4",
  "org.jsoup" % "jsoup" % "1.7.2",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test",
  "junit" % "junit" % "4.11" % "test",
  "org.mockito" % "mockito-all" % "1.8.4" % "test"
)


libraryDependencies ++= Seq(
  "net.databinder" %% "unfiltered-filter" % "0.8.1",
  "net.databinder" %% "unfiltered-netty-server" % "0.8.1",
  "net.databinder" %% "unfiltered-netty-websockets" % "0.8.1",
  "net.databinder" %% "unfiltered-json4s" % "0.8.1",
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)
