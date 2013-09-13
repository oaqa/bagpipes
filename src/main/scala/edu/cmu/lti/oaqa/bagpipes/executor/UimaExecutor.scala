package edu.cmu.lti.oaqa.bagpipes.executor

import org.apache.uima.analysis_component.AnalysisComponent
import org.apache.uima.collection.CollectionReader
import org.apache.uima.UIMAFramework
import org.apache.uima.util.CasCopier
import org.uimafit.factory.AnalysisEngineFactory
import org.uimafit.factory.CollectionReaderFactory
import org.uimafit.factory.JCasFactory
import org.uimafit.factory.TypeSystemDescriptionFactory
import edu.cmu.lti.oaqa.bagpipes.util.TraceUtil
import org.apache.uima.jcas.JCas

/**
 * @author Collin McCormack
 */
class UimaExecutor extends Executor[JCas, UimaComponent] {
  import UimaExecutor._
  
  // Create caches
  var data = new DataCache[JCas]()
  val comps = new ComponentCache[UimaComponent]()
  // Instance variable place holders
  var reader:Reader = _
  // Keep which input we're currently using so that no two two JCas's from any 
  // run of a pipeline will have the same trace.
  var inputCount = 0
  
  /**
   * Use a CollectionReader to put the first input item into a JCas.
   */
  def initialize(readerCls: String, params: List[(String, Any)]): Unit = {
    // Init Reader and JCas, and run the former with the latter
    reader = createReader(readerCls, params)
    val root = newJCas()
    reader.execute(root)
    inputCount += 1
    // Reader is an instance variable, no need to cache it
    // Add JCas to cache
    data + (reader.toString() + "@" + inputCount, root)
  }

  /**
   * Use an Annotator (AnalysisEngine) to process a CAS specified by the trace.
   * 
   * The Annotator processes a duplicate CAS that is the result of the upstream 
   * Annotators encoded in the trace.  The original CAS is left intact.  The 
   * newly processed CAS is then stored with an updated trace string.
   * 
   * If the Annotator and it's parameters have not been used before to 
   * instantiate an Annotator, then a new one will be created; otherwise, the 
   * existing one will be used.  If a new Annotator is created, it will be 
   * cached for later re-use.
   * 
   * @param cls
   * 	A fully-qualified classname of a UIMA AnalysisEngine
   * @param params
   * 	The parameters for the AnalysisEngine
   * @param trace
   * 	The process trace that specifies the input CAS
   */
  override def execute(cls: String, params: List[(String,Any)], trace: String) = {
    /** Retrieve the requested Annotator from the cache, otherwise create it */
    def getAnnotator(cls: String, params: List[(String,Any)]): Annotator = {
      if (comps contains UimaComponent.mkId(cls, params)) comps.get(UimaComponent.mkId(cls, params)).asInstanceOf[Annotator]
      else {
    	val annt = createAnnotator(cls, params)
    	comps + (annt.getId(), annt)  // Add to cache
    	annt
      }
    }
    
    // Get/Create the annotator, run it, and cache the output
    val annt = getAnnotator(cls, params)
    data + (TraceUtil.mkTrace(trace, annt.toString()), annt.execute(data(trace)))
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
  override def reset(cls: String, params: List[(String, Any)]): Boolean = {
    // Thought: allow for a new Writer to be instantiated for every JCas, each  
    // Writer is varied on one parameter.  This is a special case where a user 
    // may actually want to pass in some sort of unique identifier (e.g. a trace) 
    // when writing out the JCas
    
    // Create a Writer, and run it on each JCas in the data cache
    val w = createWriter(cls, params)
    data.getContents map (jcas => w.execute(jcas))
    // Destroy ALL the JCas objects in the cache
    data.destroy()
    
    // Get a fresh root JCas and data cache and try to get the next input
    // If one is available, cache it and return true; otherwise, return false
    val root = newJCas()
    data = new DataCache[JCas]()
    val hasNext = reader.execute(root)
    if (hasNext) {
      inputCount += 1
      data + (reader.toString() + "@" + inputCount, root)
    } else comps.destroy()
    hasNext
  }
  
  /** Create a new, empty JCas object */
  private def newJCas() = JCasFactory.createJCas(UimaComponent.typeSysDesc)
}

object UimaExecutor {
  @throws[ClassCastException]("If the class is not a child of CollectionReader")
  def createReader(cls: String, params: List[(String,Any)]): Reader = {
    val clazz = Class.forName(cls).asSubclass(classOf[CollectionReader])
    new Reader(clazz, params)
  }
  
  @throws[ClassCastException]("If the class is not a child of AnalysisComponent")
  def createAnnotator(cls: String, params: List[(String,Any)]): Annotator = {
    val clazz = Class.forName(cls).asSubclass(classOf[AnalysisComponent])
    new Annotator(clazz, params)
  }
  
  @throws[ClassCastException]("If the class is not a child of AnalysisComponent")
  def createWriter(cls: String, params: List[(String,Any)]): Writer = {
    val clazz = Class.forName(cls).asSubclass(classOf[AnalysisComponent])
    new Writer(clazz, params)
  }
}