package edu.cmu.lti.oaqa.bagpipes.executor.uima
import org.apache.uima.jcas.JCas
import org.apache.uima.fit.factory.CollectionReaderFactory
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import UimaReader._
import edu.cmu.lti.oaqa.bagpipes.executor.Reader
import org.apache.uima.collection.CollectionReader
import org.apache.uima.fit.factory.JCasFactory
import org.apache.uima.util.CasCopier
/**
 * Instantiate and use any arbitrary UIMA Collection Reader to feed a pipeline.
 *
 * @param readerDesc
 * 		A CollectionReaderDescriptor containing the classpath and parameters for
 *      the CollectionReader.
 * @author Collin McCormack, and Avner Maiberg
 */
final class UimaReader(readerDesc: CollectionReaderDescriptor) extends UimaComponent(readerDesc) with Reader[JCas] {
  //import UimaComponent.flattenParams

  // Create the Collection Reader internally

  val collReaderClass = createReaderClass(className)
  println("params: " + params)
  val collReader = CollectionReaderFactory.createReader(collReaderClass, typeSysDesc, params: _*)

  /**
   * Try to put the next input item into the JCas
   *
   * @param input
   * 	The JCas to populate with the next input item
   * @return true if there was another item use, false otherwise
   */
  override def executeComponent(input: JCas): Unit =  collReader.getNext(input.getCas())
  

  /**
   * Close and release the resources for the CollectionReader.
   */
  override def destroy() = {
    collReader.close()
    collReader.destroy()
  }
}

object UimaReader {

  protected def createReaderClass(className: String) = Class.forName(className).asInstanceOf[Class[_ <: CollectionReader]]

}