/*
 * Copyright (c) 2015, Michael Lewis
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.waioeka.sbt.runner

import cucumber.runtime.{Runtime, RuntimeOptions}
import cucumber.runtime.io.{MultiLoader, ResourceLoaderClassFinder}
import sbt.testing._

import scala.util.Try

/**
  * CucumberFeatureRunner
  *   This class implements the Runner2 interface for running Cucumber tests.
  */
class CucumberFeatureRunner(classLoader: ClassLoader, override val args: Array[String], override val remoteArgs: Array[String]) extends Runner {

  def tasks(taskDefs: Array[TaskDef]): Array[Task] = taskDefs.map{ taskDefIn =>
    new Task {
      val tags: Array[String] = Array.empty

      val taskDef: TaskDef = taskDefIn

      override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
        loggers foreach (_.debug("[CucumberFramework.testRunner] Creating Cucumber test runner."))
        run(taskDef.fullyQualifiedName(), eventHandler, loggers, taskDef.fingerprint())
        Array.empty
      }
    }
  }


  /**
    * Run a Cucumber test.
    *
    * @param testName         the name of the test.
    * @param eventHandler     the event handler.
    */
  def run(testName: String, eventHandler : EventHandler, loggers: Array[Logger], fingerprint: Fingerprint) : Unit = {

    def logDebug(s: String) : Unit = loggers foreach(_ debug s)

    Try {
      execute( classLoader) match {
        case 0 =>
          logDebug(s"[CucumberFeatureRunner.run] Cucumber test $testName completed successfully.")
          eventHandler.handle(SuccessEvent(testName, fingerprint))
        case _ =>
          logDebug(s"[CucumberFeatureRunner.run] Cucumber test $testName  failed.")
          eventHandler.handle(FailureEvent(testName, fingerprint))
      }
    }.recover {
      case t: Throwable =>
          eventHandler.handle(ErrorEvent(testName,t, fingerprint))
    }.get
  }

  /**
    * Create the Cucumber Runtime and execute the test.

    * @param classLoader the class loader for the Runtime.
    * @return the exit status of the Cucumber Runtime.
    */
  def execute(classLoader: ClassLoader)
  : Int = {
    val arguments =
      List("--glue","") :::
      List("--plugin", "pretty") :::
      List("--plugin", "html:html") :::
      List("--plugin", "json:json") :::
      List("classpath:")
    import scala.collection.JavaConverters._

    val runtimeOptions = new RuntimeOptions(arguments.asJava)
    val resourceLoader = new MultiLoader(classLoader)
    val classFinder = new ResourceLoaderClassFinder(resourceLoader,classLoader)
    val runtime = new Runtime(
                              resourceLoader,
                              classFinder,
                              classLoader,
                              runtimeOptions)
    runtime.run()
    runtime.printSummary()
    runtime.exitStatus()
  }

  override def done(): String = ""

}
