import sbt.Keys.libraryDependencies

name := "scala-akka-crawler"

version := "1.0"

scalaVersion := "2.11.8"
retrieveManaged := true
//添加偏僻库
resolvers += "mvnrepository" at "https://mvnrepository.com/artifact/"

//仓库的配置文件是repositories
//%% 自动给库包的报名结尾加上Scala的版本号,%只用于分割groupId与artifactId
libraryDependencies ++= Seq(

    "org.apache.lucene" % "lucene-core" % "4.7.2",
    "com.typesafe.akka" %% "akka-actor" % "2.5.5",
    "org.slf4j" % "slf4j-api" % "1.7.22",
    "com.typesafe.akka" % "akka-slf4j_2.11" % "2.4.3",
    "org.htmlparser" % "htmlparser" % "2.1",
    "org.perf4j" % "perf4j" % "0.9.16",
    "org.slf4j" % "slf4j-simple" % "1.7.22",
    "junit" % "junit" % "4.12",
    "com.janeluo" % "ikanalyzer" % "2012_u6",
    "org.jsoup" % "jsoup" % "1.10.3",
    "commons-io" % "commons-io" % "2.3",
    "com.typesafe.akka" %% "akka-testkit" % "2.5.9" % Test,
    "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    "org.apache.httpcomponents" % "httpclient" % "4.5.3",
    "io.reactivex.rxjava2" % "rxjava" % "2.2.3",
    //    "javax.media" % "jai_core" % "1.1.3",//仓库文件失效了
//    "com.sun.media" % "jai-codec" % "1.1.3",
    "com.alibaba" % "simpleimage" % "1.2.3" exclude("org.slf4j", "slf4j-log4j12"),
    "org.springframework.boot" % "spring-boot-autoconfigure" % "1.5.9.RELEASE" exclude("org.springframework.boot", "spring-boot-starter-logging"),
    "org.springframework.boot" % "spring-boot-starter-web" % "1.5.9.RELEASE" exclude("org.springframework.boot", "spring-boot-starter-logging"),
    "org.springframework.boot" % "spring-boot-starter-cache" % "1.5.9.RELEASE" exclude("org.springframework.boot", "spring-boot-starter-logging")
)

//添加sbt路径

//val sbtcp = taskKey[Unit]("sbt-classpath")
//
//sbtcp := {
//    val files: Seq[File] = (fullClasspath in Compile).value.files
//    val sbtClasspath: String = files.map(x => x.getAbsolutePath).mkString(":")
//    println("Set SBT classpath to 'sbt-classpath' environment variable")
//    System.setProperty("sbt-classpath", sbtClasspath)
//}
//
////<<=已经过期
//compile := {
//    ((compile in Compile) dependsOn sbtcp).value
//}