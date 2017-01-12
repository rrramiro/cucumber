
lazy val root = (project in file(".")).settings(
  name := "cucumber-plugin",

  organization := "com.waioeka.sbt",

sbtPlugin := true,

  scalaVersion := "2.10.6",

version := "0.0.10-SNAPSHOT",

libraryDependencies ++= Seq (
  "info.cukes" % "cucumber-core" % "1.2.4",
  "info.cukes" % "cucumber-jvm" % "1.2.4",
  "info.cukes" % "cucumber-junit" % "1.2.4",
  "org.apache.commons" % "commons-lang3" % "3.4"),


pomIncludeRepository := { _ => false },

pomExtra := (
<url>https://github.com/lewismj/cucumber</url>
<licenses>
  <license>
    <name>BSD-style</name>
    <url>http://www.opensource.org/licenses/bsd-license.php</url>
    <distribution>repo</distribution>
  </license>
</licenses>
<scm>
  <url>git@github.com:lewismj/cucumber.git</url>
  <connection>scm:git:git@github.com:lewismj/cucumber.git</connection>
</scm>
<developers>
  <developer>
    <id>lewismj</id>
    <name>Michael Lewis</name>
    <url>http://www.waioeka.com</url>
  </developer>
</developers>),

publishMavenStyle := true,

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

)

lazy val cucumberRunner = (project in file("cucumber-runner")).settings(
  name := "cucumber-runner",

  organization := "com.waioeka.sbt",

scalaVersion := "2.11.8",

crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.1"),

version := "0.0.6-SNAPSHOT",

libraryDependencies ++= Seq (
  "info.cukes" % "cucumber-core" % "1.2.4",
  "info.cukes" %% "cucumber-scala" % "1.2.4",
  "info.cukes" % "cucumber-jvm" % "1.2.4",
  "info.cukes" % "cucumber-junit" % "1.2.4",
  "org.scala-sbt" % "test-interface" % "1.0",
  "org.scalatest"   %% "scalatest" % "3.0.0" % "test"
),

pomIncludeRepository := { _ => false },

pomExtra := (
<url>https://github.com/lewismj/cucumber</url>
<licenses>
  <license>
    <name>BSD-style</name>
    <url>http://www.opensource.org/licenses/bsd-license.php</url>
    <distribution>repo</distribution>
  </license>
</licenses>
<scm>
  <url>git@github.com:lewismj/cucumber.git</url>
  <connection>scm:git:git@github.com:lewismj/cucumber.git</connection>
</scm>
<developers>
  <developer>
    <id>lewismj</id>
    <name>Michael Lewis</name>
    <url>http://www.waioeka.com</url>
  </developer>
</developers>),


publishMavenStyle := true,


publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
},

testFrameworks += new TestFramework("com.waioeka.sbt.runner.CucumberFramework")

)

