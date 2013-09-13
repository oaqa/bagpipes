package edu.cmu.lti.oaqa.bagpipes.executor

import org.apache.uima.collection.CollectionReader
import org.apache.uima.jcas.JCas
import org.uimafit.factory.CollectionReaderFactory
import org.uimafit.factory.JCasFactory

/**
 * Instantiate and use any arbitrary UIMA Collection Reader to feed a pipeline.
 * 
 * @param cls
 * 		The class of the CollectionReader to use
 * @param params
 * 		The parameters for the CollectionReader
 * @author Collin McCormack
 */
class Reader(cls: Class[_ <: CollectionReader], params: List[(String, Any)]) extends UimaComponent(cls, params) {
  import UimaComponent.flattenParams
  
  // Create the Collection Reader internally
  val collReader = CollectionReaderFactory.createCollectionReader(cls, UimaComponent.typeSysDesc, params:_*)
  
  /**
   * Try to put the next input item into the JCas
   * 
   * @param input
   * 	The JCas to populate with the next input item
   * @return true if there was another item use, false otherwise
   */
  override def execute(input: JCas): Boolean = {
    val flag = collReader.hasNext()
    if (flag) collReader.getNext(input.getCas())
    flag
  }
  
  /**
   * Close and release the resources for the CollectionReader.
   */
  override def destroy() = {
    collReader.close()
    collReader.destroy()
  }
}