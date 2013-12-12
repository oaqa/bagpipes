package edu.cmu.lti.oaqa.bagpipes.executor

import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
//case class Input(inputNum: Int, input: I)
case class Trace(inputNum: Int=0, componentTrace: Stream[AtomicExecutableConf]) {
  def ++(execDesc: AtomicExecutableConf): Trace = Trace(inputNum, componentTrace #::: Stream(execDesc))
  def getInputNum: Int = inputNum
  def getTraceList: List[AtomicExecutableConf] = componentTrace.toList
  override def toString: String = "Input #: " + inputNum + "\nTrace: " + componentTrace.toList
}

case class ExecutionResult[I](result: I, input: Int)
