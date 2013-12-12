package edu.cmu.lti.oaqa.bagpipes.executor

import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.db.BagpipesDatabase.Metric
trait ExecutorTypes[I, C <: ExecutableComponent[I]]  {

  type ComponentCache = Map[Stream[AtomicExecutableConf], C]
  type DataCache = Map[Trace, Result[I]]
  case class Cache(dataCache: DataCache, componentCache: ComponentCache) {
    def ++(cache: Cache) = this match {
      case Cache(d, c) => Cache(d ++ cache.dataCache, c ++ cache.componentCache)
    }
  }

  def updateCache(newInput: Result[I], newComponent: C, trace: Trace)(implicit cache: Cache) = cache match {
    case Cache(dataCache, compCache) => Cache(dataCache ++ Map(trace -> newInput), compCache ++ Map(trace.componentTrace -> newComponent))
  }
}
