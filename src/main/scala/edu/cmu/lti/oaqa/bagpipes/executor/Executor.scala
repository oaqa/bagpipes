package edu.cmu.lti.oaqa.bagpipes.executor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors._
import scala.collection.immutable.Stream.consWrapper
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.annotation.Analytic
import edu.cmu.lti.oaqa.bagpipes.annotation.SimpleAnalytic
/**
 * A generic strategist that does all the high-level "bookkeeping"
 * for execution pipelines (i.e., keeping track of the trace (and subtraces),
 * updating the result and component cache, executing annotators, and storing
 * their results). The set of all executions are closed under the type `I`. `C`
 * is a either a Reader or Annotator that may be readily executed.
 *
 * @param I
 * 		Data exchange type
 * @param C
 * 		ExecutableComponent parent type
 */
trait Executor[I, C <: ExecutableComponent[I]] extends ExecutorTypes[I, C] {

  protected val componentFactory: ComponentFactory[I, C]

  def getFirstInput: Result[I]
  final def getEmptyCache(input: Int) = Cache(Map(Trace(input, Stream()) -> getFirstInput), Map())
  final def getComponentFactory = componentFactory
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
  final def execute(execDesc: AtomicExecutableConf, trace: Trace)(implicit cache: Cache): (Result[I], Cache) = {
    val newTrace: Trace = trace ++ execDesc // update trace
    val component: C = if (cache.componentCache.contains(newTrace.componentTrace))
      cache.componentCache(newTrace.componentTrace) // get cached component
    else 
      componentFactory.create(execDesc) // create new component from factory TODO: put this in factory
    val prevExecResult: Result[I] = cache.dataCache(trace) //get previous result up to current sub-trace
    val execResult: Result[I] = component.execute(prevExecResult) // execute using previous result as input
    val updatedCache: Cache = updateCache(Result[I](execResult.getInput)(SimpleAnalytic(Nil)), component, newTrace)(cache) // update the cache
    val result = (execResult, updatedCache) // aggregate result with cache
    result // return result with cache for use in calling controller
  }

  def reset(cls: String, params: List[(String, Any)]): Boolean
}



