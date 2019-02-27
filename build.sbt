name := "scala-akka-crawler"

version := "1.0"

scalaVersion := "2.11.8"

retrieveManaged := true

libraryDependencies += "org.apache.lucene" % "lucene-core" % "4.7.2"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.5"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.22"

libraryDependencies += "org.htmlparser" % "htmlparser" % "2.1"

libraryDependencies += "org.perf4j" % "perf4j" % "0.9.16"

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.22"

libraryDependencies += "junit" % "junit" % "4.12"

libraryDependencies += "com.janeluo" % "ikanalyzer" % "2012_u6"

libraryDependencies += "org.jsoup" % "jsoup" % "1.10.3"

