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
  *   This class implements the Runner interface for running Cucumber tests.
  */
class CucumberFeatureRunner(classLoader: ClassLoader, override val args: Array[String], override val remoteArgs: Array[String]) extends Runner {

  private val arguments =
    Seq("--glue", "") ++
    Seq("--plugin", "pretty") ++
    Seq("--plugin", "html:html") ++
    Seq("--plugin", "json:json") ++
    Seq("classpath:")

  def tasks(taskDefs: Array[TaskDef]): Array[Task] = taskDefs.map{ taskDefIn =>
    new Task {
      val tags: Array[String] = Array.empty

      val taskDef: TaskDef = taskDefIn

      override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
        def logDebug(s: String) : Unit = loggers foreach{_ debug s}

        logDebug("[CucumberFramework.testRunner] Creating Cucumber test runner.")
        val testName = taskDef.fullyQualifiedName()
        Try {
          executeCucumber(arguments, classLoader) match {
            case 0 =>
              logDebug(s"[CucumberFeatureRunner.run] Cucumber test $testName completed successfully.")
              eventHandler.handle(SuccessEvent(testName, taskDef.fingerprint()))
            case _ =>
              logDebug(s"[CucumberFeatureRunner.run] Cucumber test $testName  failed.")
              eventHandler.handle(FailureEvent(testName, taskDef.fingerprint()))
          }
        }.recover {
          case t: Throwable =>
            eventHandler.handle(ErrorEvent(testName,t, taskDef.fingerprint()))
        }.get
        Array.empty
      }
    }
  }

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

  override def done(): String = ""

}
