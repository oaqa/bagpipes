package edu.cmu.lti.oaqa.bagpipes.executor

import org.apache.uima.jcas.JCas
import org.uimafit.factory.AnalysisEngineFactory
import org.apache.uima.UIMAFramework
import org.apache.uima.analysis_component.AnalysisComponent

/**
 * Instantiate and use any UIMA AnalysisEngine in a pipeline.
 * 
 * The only difference between this and an Annotator is that this does not
 * process a duplicated JCas, it works on the original input and does not return
 * a new JCas.  Therefore, any AnalysisEngine that is instantiated as a Writer 
 * should NOT make any changes to the input JCas; additionally, it should be 
 * state-less (i.e. capable of running multiple times on different inputs with
 * no side effects). 
 * 
 * @param cls
 * 		The class of the UIMA AnalysisEngine to use
 * @param params
 * 		The parameters for the AnalysisEngine
 * @author Collin McCormack
 */
class Writer(cls: Class[_ <: AnalysisComponent], params: List[(String, Any)]) extends Annotator(cls, params) {
  override def execute(jcas: JCas): JCas = {
    ae.process(jcas)
    jcas  // Only to maintain compatibility with Annotator signatures
  }
}