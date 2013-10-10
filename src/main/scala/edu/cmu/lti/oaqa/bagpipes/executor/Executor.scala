package edu.cmu.lti.oaqa.bagpipes.executor

import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ComponentDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.AtomicExecutable
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.AtomicExecutableConf

/**
 * @param I
 * 		Data exchange type
 * @param C
 * 		ExecutableComponent parent type
 */
trait Executor[I, C <: ExecutableComponent[I]] extends ExecutorTypes[I, C] {

  protected val componentFactory: ComponentFactory[I, C]

  protected def getFirstInput: I
  final def getEmptyCache = Cache(Map(Trace(0, Stream()) -> getFirstInput), Map())

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

protected abstract trait ComponentFactory[I, C <: ExecutableComponent[I]] {
  def create(execDesc: AtomicExecutable): C = execDesc match {
    case collDesc @ CollectionReaderDescriptor(_, _) => createReader(collDesc)
    case compDesc @ ComponentDescriptor(_, _) => createAnnotator(compDesc)
  }
  def createReader(readerDesc: CollectionReaderDescriptor): Reader[I] with C
  def createAnnotator(componentDesc: ComponentDescriptor): Annotator[I] with C
}

trait ExecutorTypes[I, C <: ExecutableComponent[I]] {
  case class Trace(inputNum: Int, componentTrace: Stream[AtomicExecutable]) {
    def ++(execDesc: AtomicExecutable): Trace = Trace(inputNum, componentTrace #::: Stream(execDesc))
    override def toString: String = "Input #: " + inputNum + "\nTrace: " + componentTrace
  }

  type ComponentCache = Map[Trace, C]
  type DataCache = Map[Trace, I]
  type Result = (I, Cache)

  case class Cache(dataCache: DataCache, componentCache: ComponentCache)

  def updateCache(newInput: I, newComponent: C, trace: Trace)(implicit cache: Cache) = cache match {
    case Cache(dataCache, compCache) => Cache(dataCache ++ Map(trace -> newInput), compCache ++ Map(trace -> newComponent))
  }
}