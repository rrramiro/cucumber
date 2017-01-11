name := "cucumber-test2"

scalaVersion := "2.11.8"

version := "0.0.1"

resolvers += Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq (
        "info.cukes" % "cucumber-core" % "1.2.4" % "test",
        "info.cukes" %% "cucumber-scala" % "1.2.4" % "test",
        "info.cukes" % "cucumber-jvm" % "1.2.4" % "test",
        "info.cukes" % "cucumber-junit" % "1.2.4" % "test",
        "com.waioeka.sbt" %% "cucumber-runner" % "0.0.6-SNAPSHOT",
        "org.scalatest" %% "scalatest" % "3.0.0" % "test")

enablePlugins(CucumberPlugin)

CucumberPlugin.glue := "com/waioeka/sbt/"

testFrameworks += new TestFramework("com.waioeka.sbt.runner")
