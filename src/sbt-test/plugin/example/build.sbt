name := "cucumber-test"

organization := "com.waioeka.sbt"

version := "0.0.2"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq (
        "info.cukes" % "cucumber-core" % "1.2.4" % "test",
        "info.cukes" %% "cucumber-scala" % "1.2.4" % "test",
        "info.cukes" % "cucumber-jvm" % "1.2.4" % "test",
        "info.cukes" % "cucumber-junit" % "1.2.4" % "test",
        "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

enablePlugins(CucumberPlugin)

CucumberPlugin.glue := "com/waioeka/sbt/"

def beforeAll() : Unit = { println("** hello **") }
def afterAll() : Unit = { println("** goodbye **") }

CucumberPlugin.beforeAll := beforeAll
CucumberPlugin.afterAll := afterAll

