package com.waioeka.sbt.runner

import cucumber.runtime.{Runtime, RuntimeOptions}
import cucumber.runtime.io.{MultiLoader, ResourceLoaderClassFinder}

/**
  * Created by ramiro on 12/01/17.
  */
object CucumberExecuter {
  /**
    * Create the Cucumber Runtime and execute the test.
    *
    * @param arguments cucumber arguments
    * @param classLoader the class loader for the Runtime.
    * @return the exit status of the Cucumber Runtime.
    */
  def executeCucumber(arguments: Seq[String], classLoader: ClassLoader): Int = {
    import scala.collection.JavaConverters._
    val resourceLoader = new MultiLoader(classLoader)
    val runtime = new Runtime(resourceLoader, new ResourceLoaderClassFinder(resourceLoader,classLoader), classLoader, new RuntimeOptions(arguments.asJava))
    runtime.run()
    runtime.printSummary()
    runtime.exitStatus()
  }
}
