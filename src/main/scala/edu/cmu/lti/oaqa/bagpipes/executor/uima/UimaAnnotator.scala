package edu.cmu.lti.oaqa.bagpipes.executor.uima
import org.apache.uima.UIMAFramework
import org.apache.uima.jcas.JCas
import org.apache.uima.util.CasCopier
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.JCasFactory
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import UimaAnnotator._
import edu.cmu.lti.oaqa.bagpipes.executor.Annotator
import edu.cmu.lti.oaqa.bagpipes.executor.uima._
import org.apache.uima.analysis_component.AnalysisComponent
import edu.cmu.lti.oaqa.bagpipes.executor.Result
/**
 * Instantiate and use any UIMA AnalysisEngine in a pipeline.
 *
 * @param compDesc
 * 		A ComponentDescriptor with the class and parameters of the UIMA
 *      AnalysisEngine to use.
 * @author Collin McCormack, and Avner Maiberg (amaiberg@cs.cmu.edu)
 */
final class UimaAnnotator(compDesc: ComponentDescriptor) extends UimaComponent(compDesc) with Annotator[JCas] {
  // Create AnalysisEngine internally

  val componentClass = createAnalysisComponentClass(className)
  println("annotator params: " + params)
  val aeDescriptor = AnalysisEngineFactory.createPrimitiveDescription(componentClass, typeSysDesc, params: _*)
  val ae = UIMAFramework.produceAnalysisEngine(aeDescriptor)

  /**
   * Process a copy of the input JCas with this AnalysisEngine.
   *
   * @param input
   * 	The JCas to process
   * @return A process copy of the input JCas
   */ 
  override def executeComponent(input: Result[JCas]): Result[JCas] = {
    val result @ Result(cas) = input
    ae.process(cas.getCas())
    result
  }

  /**
   * Clean-up and release resources.
   */
  override def destroy() = {
    ae.collectionProcessComplete()
    ae.destroy()
  }
}

object UimaAnnotator {
  private def createAnalysisComponentClass(className: String) = Class.forName(className).asInstanceOf[Class[_ <: AnalysisComponent]]
}