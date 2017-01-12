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

import sbt.testing._

import scala.util.Try

/**
 * CucumberFramework
 *
 *   An implementation of the Framework interface for Cucumber feature file
 *   tests.
 */
class CucumberFramework extends Framework {
  /** The name of the test framework. */
  val name = "cucumber"

  /**
    *  The array of Cucumber fingerprint(s).
    *   Identifies a test class that has a specific type of ancestor.
    *   For Cucumber tests in this plugin, this is defined by the
    *   'CucumberRunner' trait.
    */
  val fingerprints: Array[Fingerprint] = Array(new SubclassFingerprint {
    /**  Whether a test is a module or a class. */
    val isModule = false

    /** The name of the type that designates a test.*/
    val superclassName: String = classOf[CucumberRunner].getName

    val requireNoArgConstructor: Boolean = false
  })

  def runner(argsIn: Array[String], remoteArgsIn: Array[String], testClassLoader: ClassLoader): Runner = new Runner {
    val done: String = ""
    val args: Array[String] = argsIn
    val remoteArgs: Array[String] = remoteArgsIn

    private val arguments =
      Seq("--glue", "") ++
        Seq("--plugin", "pretty") ++
        Seq("--plugin", "html:target/html") ++
        Seq("--plugin", "json:target/json") ++
        Seq("classpath:")

    def tasks(taskDefs: Array[TaskDef]): Array[Task] = taskDefs.map { taskDefIn =>
      new Task {
        val tags: Array[String] = Array.empty

        val taskDef: TaskDef = taskDefIn

        override def execute(eventHandler: EventHandler, loggers: Array[Logger]): Array[Task] = {
          def logDebug(s: String): Unit = loggers foreach { _ debug s }

          logDebug("[CucumberFramework.testRunner] Creating Cucumber test runner.")
          val testName = taskDef.fullyQualifiedName()
          Try {
            CucumberExecuter.executeCucumber(arguments, testClassLoader) match {
              case 0 =>
                logDebug(s"[CucumberFramework.run] Cucumber test $testName completed successfully.")
                eventHandler.handle(SuccessEvent(testName, taskDef.fingerprint()))
              case _ =>
                logDebug(s"[CucumberFramework.run] Cucumber test $testName  failed.")
                eventHandler.handle(FailureEvent(testName, taskDef.fingerprint()))
            }
          }.recover {
            case t: Throwable =>
              eventHandler.handle(ErrorEvent(testName, taskDef.fingerprint(), Some(t)))
          }.get
          Array.empty
        }
      }
    }
  }
}
