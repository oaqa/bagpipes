package edu.cmu.lti.oaqa.bagpipes.controller
import edu.cmu.lti.oaqa.bagpipes.space.explorer.Explorer
import edu.cmu.lti.oaqa.bagpipes.executor.Executor
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutableComponent
import edu.cmu.lti.oaqa.bagpipes.space._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors._
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.scorer.Scorer
import edu.cmu.lti.oaqa.bagpipes.scorer.DefaultScorer
import edu.cmu.lti.oaqa.bagpipes.space._
import edu.cmu.lti.oaqa.bagpipes.executor.Result
import scala.collection.immutable.Stream.consWrapper
import edu.cmu.lti.oaqa.bagpipes.executor.Trace

/**
 *
 *
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */ 

class ExecutionController[I](explr: Explorer[CollectionReaderDescriptor, AtomicExecutableConf, Int], exctr: Executor[_, _ <: ExecutableComponent[_]])(implicit scorer: Scorer[_] = DefaultScorer[I]) {
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

    def execute(compDesc: TreeWithHistory[AtomicExecutableConf], input: Int)(implicit cache: exctr.Cache) = compDesc match {
      case (compDesc @ TreeWithHistory(elem, hist)) =>
        println("executing component (with history): " + compDesc)
        val result @ (Result(res), _) = exctr.execute(elem, Trace(input, hist))(cache)
        println("result:  " + res)
        result
    }

    type ExecutionResult = (Result[_], exctr.Cache)
    def getBlankResult(inputNum: Int)(implicit cache: exctr.Cache = exctr.getEmptyCache(inputNum)) =
      (exctr.getFirstInput, cache ++ exctr.getEmptyCache(inputNum))
    def execStream(execStrm: Stream[explr.ExecutableTree], inputNum: Int)(implicit cache: ExecutionResult = getBlankResult(inputNum)): ExecutionResult = (execStrm, inputNum, cache) match {
      //finished all inputs return final cache
      case (Stream(), 0, _) =>
        println("done")
        cache
      //finished on given input  
      case (Stream(), input, (result, cache)) =>
        println("finished input: #" + input)
        //maybe do stuff at end of the input
        //Move to next input from reader.
        //create new "blank" input for next execution stream
        val newExecResult = getBlankResult(input - 1)
        execStream(confSpaceStream, input - 1)(newExecResult)
      //root or node (non-leaf)
      case ((compDesc @ TreeWithHistory(elem, hist)) #:: rest, input, (_, cache)) =>
        val execResult = execute(compDesc, input)(cache)
        //maybe do something with result here
        execStream(nonRepeating(explr.from(rest)(input)), input)(execResult)

      /*      //path from root-to-leaf
      case ((compDesc @ Leaf(elem, hist)) #:: rest, input, (res, cache)) => {
        val (result, updatedCache) = execute(compDesc,input)(cache)
        //maybe do something with result here
        
        execStream(nonRepeating(explr.from(rest)(input)), input)(res, updatedCache)
      }
*/
    }
    val res = execStream(confSpaceStream, totalInputs - 1)
    //maybe do more stuff with result + cache

  }
}

object ExecutionController {
  def apply[I](explr: Explorer[CollectionReaderDescriptor, AtomicExecutableConf, Int], exctr: Executor[I, _ <: ExecutableComponent[I]])(implicit scorer: Scorer[I] = DefaultScorer[I]) =
    new ExecutionController(explr, exctr)

  //return a stream with non-repeating components
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

