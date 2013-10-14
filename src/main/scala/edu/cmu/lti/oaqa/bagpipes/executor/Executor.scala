package edu.cmu.lti.oaqa.bagpipes.executor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.AtomicExecutable
import scala.collection.immutable.Stream.consWrapper

/**
 * A generic strategist that does all the high-level "bookkeeping"
 * for execution pipelines (i.e., keeping track of the trace (and subtraces),
 * updating the result and component cache, executing annotators, and storing
 * their results).`I` is the type for which the set of all executions
 * are "closed under". `C` is a either a Reader or Annotator that may be readily
 * executed.
 *
 * @param I
 * 		Data exchange type
 * @param C
 * 		ExecutableComponent parent type
 */
trait Executor[I, C <: ExecutableComponent[I]] extends ExecutorTypes[I, C] {

  protected val componentFactory: ComponentFactory[I, C]

  protected def getFirstInput: I
  final def getEmptyCache = Cache(Map(Trace(0, Stream()) -> getFirstInput), Map())

  /**
   * Use a component C, to process an input I, specified by the trace.
   *
   * If the trace, component, and its parameters have not been used before to
   * instantiate, then a new one will be created; otherwise, the
   * existing one will be used.  If a new component is created, it will be
   * cached for later re-use.
   *
   * @param execDesc
   * 	A descriptor containing the classname and parameters for some analysis engine
   * @param trace
   * 	The process trace of components associated with the next input
   */
  //def execute(execDesc: ExecutableConf, trace: Trace) = ???
  final def execute(execDesc: AtomicExecutable, trace: Trace)(implicit cache: Cache): Result = {
    val newTrace: Trace = trace ++ execDesc // update trace
    val component: C = if (cache.componentCache.contains(newTrace))
      cache.componentCache(newTrace) // get cached component
    else
      componentFactory.create(execDesc) // create new component from factory TODO: put this in factory
    val prevExecResult: I = cache.dataCache(trace) //get previous result up to current sub-trace
    val execResult: I = component.execute(prevExecResult) // execute using previous result as input
    val updatedCache: Cache = updateCache(execResult, component, newTrace)(cache) // update the cache
    val result: Result = (execResult, updatedCache) // aggregate result with cache
    result // return result with cache for use in calling controller
  }
  def reset(cls: String, params: List[(String, Any)]): Boolean
}



