package edu.cmu.lti.oaqa.bagpipes.executor

import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.AtomicExecutable

trait ExecutorTypes[I, C <: ExecutableComponent[I]] {
  case class Trace(inputNum: Int, componentTrace: Stream[AtomicExecutable]) {
    def ++(execDesc: AtomicExecutable): Trace = Trace(inputNum, componentTrace #::: Stream(execDesc))
    override def toString: String = "Input #: " + inputNum + "\nTrace: " + componentTrace.toList
  }

  type ComponentCache = Map[Trace, C]
  type DataCache = Map[Trace, I]
  type Result = (I, Cache)

  case class Cache(dataCache: DataCache, componentCache: ComponentCache)

  def updateCache(newInput: I, newComponent: C, trace: Trace)(implicit cache: Cache) = cache match {
    case Cache(dataCache, compCache) => Cache(dataCache ++ Map(trace -> newInput), compCache ++ Map(trace -> newComponent))
  }
}
