import sbt.Keys.libraryDependencies

name := "scala-akka-crawler"

version := "1.0"

scalaVersion := "2.11.8"

retrieveManaged := true

// "org.apache.lucene" % "lucene-core" % "4.7.2"
//
// "com.typesafe.akka" %% "akka-actor" % "2.5.5"
//
// "org.slf4j" % "slf4j-api" % "1.7.22"
//
// "org.htmlparser" % "htmlparser" % "2.1"
//
// "org.perf4j" % "perf4j" % "0.9.16"
//
// "org.slf4j" % "slf4j-simple" % "1.7.22"
//
// "junit" % "junit" % "4.12"
//
// "com.janeluo" % "ikanalyzer" % "2012_u6"
//
// "org.jsoup" % "jsoup" % "1.10.3"
//
// "commons-io" % "commons-io" % "2.3"
//
// "com.typesafe.akka" %% "akka-testkit" % "2.5.9" % Test
//
// "org.scalatest" %% "scalatest" % "3.0.5" % Test

//%% 自动给库包的报名结尾加上Scala的版本号,%只用于分割groupId与artifactId
libraryDependencies ++= Seq(

    "org.apache.lucene" % "lucene-core" % "4.7.2",
    "com.typesafe.akka" %% "akka-actor" % "2.5.5",
    "org.slf4j" % "slf4j-api" % "1.7.22",
    "org.htmlparser" % "htmlparser" % "2.1",
    "org.perf4j" % "perf4j" % "0.9.16",
    "org.slf4j" % "slf4j-simple" % "1.7.22",
    "junit" % "junit" % "4.12",
    "com.janeluo" % "ikanalyzer" % "2012_u6",
    "org.jsoup" % "jsoup" % "1.10.3",
    "commons-io" % "commons-io" % "2.3",
    "com.typesafe.akka" %% "akka-testkit" % "2.5.9" % Test,
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
)