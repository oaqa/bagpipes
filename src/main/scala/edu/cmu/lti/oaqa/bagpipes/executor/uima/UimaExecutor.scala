package edu.cmu.lti.oaqa.bagpipes.executor.uima
import org.apache.uima.jcas.JCas
import org.apache.uima.fit.factory.JCasFactory
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ComponentDescriptor
import edu.cmu.lti.oaqa.bagpipes.executor._
import edu.cmu.lti.oaqa.bagpipes.executor.uima._
/**
 * @author Collin McCormack
 */
class UimaExecutor(readerDesc: CollectionReaderDescriptor) extends Executor[JCas, UimaComponent] {
  override val componentFactory = ???
  //need to rethink reset
  def reset(cls: String, a: List[(String, Any)]) = ???
  def getFirstInput = ???
  //import UimaExecutor._
  /*
  // Create caches
  var data: DataCache = DataCache[JCas](Map())
  val comps = new ComponentCache[UimaComponent]()
  // Instance variable place holders
  */

  /**
   * Use a CollectionReader to put the first input item into a JCas.
   */
  // Init Reader and JCas, and run the former with the latter
  /*
  val reader = createReader(readerDesc)
  // Keep which input we're currently using so that no two two JCas's from any 
  // run of a pipeline will have the same trace.
  var inputCount = 0

  val root = newJCas()
  reader.execute(root)
  inputCount += 1
  */
  // Reader is an instance variable, no need to cache it
  // Add JCas to cache
  // data + (reader.toString() + "@" + inputCount, root)

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
  def execute(componentDesc: ComponentDescriptor, trace: String)(implicit cache: Cache) = ??? /* {
    /** Retrieve the requested Annotator from the cache, otherwise create it */
    def getAnnotator(componentDesc: ComponentDescriptor): Annotator = {
      val className = componentDesc.`class`
      val params = componentDesc.params.toList
      if (comps contains UimaComponent.mkId(className, params)) comps.get(UimaComponent.mkId(className, params)).asInstanceOf[Annotator]
      else {
        val annt = createAnnotator(componentDesc)
        comps + (annt.getId(), annt) // Add to cache
        annt
      }
    }

    // Get/Create the annotator, run it, and cache the output
    val annt = getAnnotator(componentDesc)
    data + (TraceUtil.mkTrace(trace, annt.toString()), annt.execute(data(trace)))
  }*/

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
  // def reset(cls: String, params: List[(String, Any)]) =  ??? //{
  // Thought: allow for a new Writer to be instantiated for every JCas, each  
  // Writer is varied on one parameter.  This is a special case where a user 
  // may actually want to pass in some sort of unique identifier (e.g. a trace) 
  // when writing out the JCas

  // Create a Writer, and run it on each JCas in the data cache
  //val w = createWriter(cls, params)
  // data.getContents map (jcas => w.execute(jcas))
  // Destroy ALL the JCas objects in the cache
  //data.destroy()

  // Get a fresh root JCas and data cache and try to get the next input
  // If one is available, cache it and return true; otherwise, return false
  // val root = newJCas()
  //data = ??? //DataCache[JCas]()
  // val hasNext = reader.execute(root)
  //if (hasNext) {
  //inputCount += 1
  //  data + (reader.toString() + "@" + inputCount, root)
  //} else comps.destroy()
  //hasNext
  ???
}

/** Create a new, empty JCas object */
//
//}

object UimaExecutor extends Executor[JCas, UimaComponent] {
  private def newJCas() = JCasFactory.createJCas(UimaComponent.getTypeSystem)
  override def reset(cls: String, params: List[(String, Any)]) = ???
  override val componentFactory: ComponentFactory[JCas, UimaComponent] = UimaComponentFactory
  override def getFirstInput: JCas = newJCas()

  object UimaComponentFactory extends ComponentFactory[JCas, UimaComponent] {
    @throws[ClassCastException]("If the class is not a child of CollectionReader")
    override def createReader(readerDesc: CollectionReaderDescriptor) = new UimaReader(readerDesc)

    @throws[ClassCastException]("If the class is not a child of AnalysisComponent")
    def createAnnotator(componentDesc: ComponentDescriptor): UimaAnnotator = new UimaAnnotator(componentDesc)
  }
}