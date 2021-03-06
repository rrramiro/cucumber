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

sealed trait CucumberEvent extends Event {
  val description: String
  val testName: String
  val error: Option[Throwable]
  val duration: Long = -1
  def fullyQualifiedName: String = description
  def throwable: OptionalThrowable = error match {
    case Some(e) => new OptionalThrowable(e)
    case _       => new OptionalThrowable
  }
  def selector: Selector = new TestSelector(testName)
}

case class SuccessEvent(testName: String, override val fingerprint: Fingerprint) extends CucumberEvent {
  val description = s"[CucumberPlugin] Test $testName passed."
  val status = Status.Success
  val error = None
}

case class FailureEvent(testName: String, override val fingerprint: Fingerprint) extends CucumberEvent {
  val description = s"[CucumberPlugin] Test $testName failed or undefined steps."
  val status = Status.Failure
  val error = None
}

case class ErrorEvent(testName: String, override val fingerprint: Fingerprint, error: Option[Throwable] = None) extends CucumberEvent {
  val description = s"[CucumberPlugin] Error caught when running Cucumber $testName : ${error.map{_.getMessage}.getOrElse("")}"
  val status = Status.Error
}
