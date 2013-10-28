package edu.cmu.lti.oaqa.bagpipes.scorer

import edu.cmu.lti.oaqa.bagpipes.executor.ExecutableComponent
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutorTypes
import edu.cmu.lti.oaqa.bagpipes.executor.Trace

abstract class Scorer[I] {

  def score(trace: Trace): Double

}

class DefaultScorer[I] extends Scorer[I] {

  //def score(trace: Trace, input: I): Double = score(trace)
  def score(trace: Trace): Double = 1

}

object DefaultScorer {
  def apply[I] = new DefaultScorer[I]
}