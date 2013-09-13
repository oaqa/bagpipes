package edu.cmu.lti.oaqa.bagpipes.executor

import org.uimafit.factory.AnalysisEngineFactory
import org.apache.uima.analysis_component.AnalysisComponent
import org.apache.uima.UIMAFramework
import org.apache.uima.jcas.JCas
import org.uimafit.factory.JCasFactory
import org.apache.uima.util.CasCopier

/**
 * Instantiate and use any UIMA AnalysisEngine in a pipeline.
 * 
 * @param cls
 * 		The class of the UIMA AnalysisEngine to use
 * @param params
 * 		The parameters for the UIMA AnalysisEngine
 * @author Collin McCormack
 */
class Annotator(cls: Class[_ <: AnalysisComponent], params: List[(String, Any)]) extends UimaComponent(cls, params) {
  import UimaComponent.flattenParams
  
  // Create AnalysisEngine internally
  val aeDescriptor = AnalysisEngineFactory.createPrimitiveDescription(cls, UimaComponent.typeSysDesc, params:_*)
  val ae = UIMAFramework.produceAnalysisEngine(aeDescriptor)
  
  /**
   * Process a copy of the input JCas with this AnalysisEngine.
   * 
   * @param input
   * 	The JCas to process
   * @return A process copy of the input JCas
   */
  override def execute(input: JCas): JCas = {
    val dupJCas = JCasFactory.createJCas(UimaComponent.typeSysDesc)
    CasCopier.copyCas(input.getCas(), dupJCas.getCas(), false)
    ae.process(dupJCas)
    dupJCas
  }
  
  /**
   * Clean-up and release resources.
   */
  override def destroy() = {
    ae.collectionProcessComplete()
    ae.destroy()
  }
}