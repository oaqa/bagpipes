package edu.cmu.lti.oaqa.bagpipes.scorer

import edu.cmu.lti.oaqa.bagpipes.executor.ExecutableComponent
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutorTypes
import edu.cmu.lti.oaqa.bagpipes.executor.Trace
import edu.cmu.lti.oaqa.bagpipes.space.TreeWithHistory
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutionResult

abstract class Scorer[I] {
  def score(trace: Trace, result: Option[I]): Double
  def score(tree: TreeWithHistory[AtomicExecutableConf], result: Option[ExecutionResult[I]]=None): Double = (tree, result) match {
    case (TreeWithHistory(elem, hist), None) => score(Trace(0, hist #::: Stream(elem)),None)
    case (TreeWithHistory(elem, hist), Some(ExecutionResult(result, input))) => score(Trace(input, hist #::: Stream(elem)), Some(result))
  }
}

class DefaultScorer[I] extends Scorer[I] {
  //def score(trace: Trace, input: I): Double = score(trace)
  override def score(trace: Trace, result: Option[I]): Double = 1

}

object DefaultScorer {
  def apply[I] = new DefaultScorer[I]
}