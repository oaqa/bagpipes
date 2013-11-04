package edu.cmu.lti.oaqa.bagpipes.controller
import edu.cmu.lti.oaqa.bagpipes.space.explorer.Explorer
import edu.cmu.lti.oaqa.bagpipes.executor.Executor
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutableComponent
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutorTypes
import edu.cmu.lti.oaqa.bagpipes.executor._
import edu.cmu.lti.oaqa.bagpipes.space._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ConfigurationDescriptor
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ComponentDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.AtomicExecutableConf
import org.apache.uima.jcas.JCas
import edu.cmu.lti.oaqa.bagpipes.scorer.Scorer
import edu.cmu.lti.oaqa.bagpipes.scorer.DefaultScorer
import edu.cmu.lti.oaqa.bagpipes.space._
import edu.cmu.lti.oaqa.bagpipes.executor.Trace

/**
 *
 *
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

class ExecutionController[I, C <: ExecutableComponent[I]](explr: Explorer[CollectionReaderDescriptor, AtomicExecutableConf, Int], exctr: Executor[I, _ <: ExecutableComponent[I]])(implicit scorer: Scorer[I] = DefaultScorer[I]) {
  import ExecutionController._
  //TODO: currently initializing collection reader twice: once in execute and again
  //when instantiating the root in the stream. Find some better way of doing this,
  //maybe by storing the first one in the cache.
  def initializeCollectionReader(confRoot: Root[CollectionReaderDescriptor, AtomicExecutableConf]) = {
    val collectionReaderDesc = confRoot.root
    exctr.getComponentFactory.createReader(collectionReaderDesc)
  }

  def execute(confSpace: Root[CollectionReaderDescriptor, AtomicExecutableConf]): Unit = {

    val collectionReader = initializeCollectionReader(confSpace)
    val totalInputs = collectionReader.getTotalInputs
    implicit val input = 0
    lazy val confSpaceStream = explr.fromRoot(confSpace)(input)

    def execStream(execStrm: Stream[explr.ExecutableTree], inputNum: Int)(implicit cache: exctr.Cache = exctr.getEmptyCache(inputNum)): exctr.Cache =
      (execStrm, inputNum) match {
        case (Stream(), 0) =>
          println("done")
          cache
        case (Stream(), input) =>
          println("finished input: #" + input)
          execStream(confSpaceStream, input - 1)(cache ++ exctr.getEmptyCache(input - 1))
        case ((compDesc @ TreeWithHistory(elem, hist)) #:: rest, input) => {
          println("executing component (with history): " + compDesc)
          // println("with cache: " + cache)
          //println("getCachedResult: " + cache.dataCache(exctr.Trace(0,hist)))
          val (result, updatedCache) = exctr.execute(elem, Trace(input, hist))(cache)
          println("result:  " + result)
          //  println("resulting cache: " + updatedCache)
          //maybe do something with result here
          //explr.from(rest)
          execStream(nonRepeating(explr.from(rest)(input)), input)(updatedCache)
        }
      }
    execStream(confSpaceStream, totalInputs - 1)
  }
}

object ExecutionController {
  def apply[I](explr: Explorer[CollectionReaderDescriptor, AtomicExecutableConf, Int], exctr: Executor[I, _ <: ExecutableComponent[I]])(implicit scorer: Scorer[I] = DefaultScorer[I]) =
    new ExecutionController(explr, exctr)

  def nonRepeating[T](strm: Stream[T]): Stream[T] = {
    def nonRepeating[T](strm: Stream[T], acc: Set[T]): Stream[T] = strm match {
      case Stream() => Stream()
      case cur #:: next =>
        if (!acc.contains(cur))
          cur #:: nonRepeating(next, acc ++ Set(cur))
        else
          nonRepeating(next, acc)
    }
    nonRepeating(strm, Set())
  }
}

