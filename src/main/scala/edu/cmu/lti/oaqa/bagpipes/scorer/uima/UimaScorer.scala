package edu.cmu.lti.oaqa.bagpipes.scorer.uima

import edu.cmu.lti.oaqa.bagpipes.scorer.Scorer
import org.apache.uima.jcas.JCas
import edu.cmu.lti.oaqa.bagpipes.executor.uima.UimaComponent
import edu.cmu.lti.oaqa.bagpipes.executor.Trace

class UimaScorer extends Scorer[JCas] {
  override def score(trace: Trace, result: Option[JCas]) = 1
}