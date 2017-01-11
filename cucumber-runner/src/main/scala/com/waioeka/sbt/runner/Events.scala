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


case class SuccessEvent(testName: String, override val fingerprint: Fingerprint) extends Event {
  val description = s"[CucumberPlugin] Test $testName passed."
  val fullyQualifiedName: String = description
  val status = Status.Success
  val duration: Long = -1
  val throwable: OptionalThrowable = new OptionalThrowable
  val selector: Selector = new TestSelector(testName)
}

case class FailureEvent(testName: String, override val fingerprint: Fingerprint) extends Event {
  val description = s"[CucumberPlugin] Test $testName failed or undefined steps."
  val fullyQualifiedName: String = description
  val status = Status.Failure
  val duration: Long = -1
  val throwable: OptionalThrowable = new OptionalThrowable
  val selector: Selector = new TestSelector(testName)
}


case class ErrorEvent(testName: String, error: Throwable, override val fingerprint: Fingerprint) extends Event {
  val description = s"[CucumberPlugin] Error caught when running Cucumber $testName : ${error.getMessage}"
  val fullyQualifiedName: String = description
  val status = Status.Error
  val duration: Long = -1
  val throwable: OptionalThrowable = new OptionalThrowable(error)
  val selector: Selector = new TestSelector(testName)
}
