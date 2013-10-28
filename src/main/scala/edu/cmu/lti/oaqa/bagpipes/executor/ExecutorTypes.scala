package edu.cmu.lti.oaqa.bagpipes.executor

import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.AtomicExecutable
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.AtomicExecutableConf

trait ExecutorTypes[I, C <: ExecutableComponent[I]]  {

  type ComponentCache = Map[Stream[AtomicExecutableConf], C]
  type DataCache = Map[Trace, I]
  type Result = (I, Cache)

  case class Cache(dataCache: DataCache, componentCache: ComponentCache) {
    def ++(cache: Cache) = this match {
      case Cache(d, c) => Cache(d ++ cache.dataCache, c ++ cache.componentCache)
    }
  }

  def updateCache(newInput: I, newComponent: C, trace: Trace)(implicit cache: Cache) = cache match {
    case Cache(dataCache, compCache) => Cache(dataCache ++ Map(trace -> newInput), compCache ++ Map(trace.componentTrace -> newComponent))
  }
}
