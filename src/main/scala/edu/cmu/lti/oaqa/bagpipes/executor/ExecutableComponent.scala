package edu.cmu.lti.oaqa.bagpipes.executor

import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.EvaluatorDescriptor
import edu.cmu.lti.oaqa.bagpipes.annotation.Analytic

abstract trait Reader[I] {
  //def hasNext() : Boolean
  //def getNext(input: I) : Unit
  def getTotalInputs(): Int
}

abstract trait Evaluator[I, T] {
 // val candidateAdapter: Adapter[I, T]
//  val gsAdapter: Adapter[I, T]
}

abstract trait Adapter[I, T] {
  def getCandidates(input: I): List[T]
}

abstract trait Annotator[I]

trait ExecutableComponent[I] extends ExecutorTypes[I, ExecutableComponent[I]] {
  def execute(input: Result[I]): Result[I]
  def destroy()
}

