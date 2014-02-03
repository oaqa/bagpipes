package edu.cmu.lti.oaqa.bagpipes.executor.uima
import org.apache.uima.jcas.JCas
import org.apache.uima.fit.factory.JCasFactory
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.executor._
import edu.cmu.lti.oaqa.bagpipes.executor.uima._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.EvaluatorDescriptor
import edu.cmu.lti.oaqa.bagpipes.annotation.QAAnalytic
/**
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

object UimaExecutor extends Executor[JCas, UimaComponent] {
  private def newJCas() = JCasFactory.createJCas(UimaComponent.getTypeSystem)
  override def reset(cls: String, params: List[(String, Any)]) = ???
  override val componentFactory: ComponentFactory[JCas, UimaComponent] = UimaComponentFactory
  override def getFirstInput: Result[JCas] = Result(newJCas())(QAAnalytic())

  object UimaComponentFactory extends ComponentFactory[JCas, UimaComponent] {
    @throws[ClassCastException]("If the class is not a child of CollectionReader")
    override def createReader(readerDesc: CollectionReaderDescriptor) = new UimaReader(readerDesc)

    @throws[ClassCastException]("If the class is not a child of AnalysisComponent")
    override def createAnnotator(componentDesc: ComponentDescriptor): UimaAnnotator = new UimaAnnotator(componentDesc)

    @throws[ClassCastException]("If the class is not a child of AnalysisComponent")
    override def createEvaluator[T](componentDesc: EvaluatorDescriptor): UimaEvaluator[T] = new UimaEvaluator[T](componentDesc)
  }

  /**
   * Reset the internal data structures of the Executor and get the next input.
   *
   * Instantiate the Writer from its class and use it to consume/process all of
   * the JCas's (intermediate and final), then destroy/release all of the JCas's.
   *
   * Then, check to see if there is another input available from the collection
   * reader.  If there is, seed a new JCAS and return true; otherwise, destroy
   * the cached AnalysisEngine objects and return false.
   *
   * @param consClass
   * 	The class of the AnalysisEngine to use
   * @param params
   * 	The parameters for the AnalysisEngine
   * @return
   * 	true if there is another input to process, false otherwise
   */

}