package edu.cmu.lti.oaqa.bagpipes.controller
import edu.cmu.lti.oaqa.bagpipes.space.Explorer
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
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Tree
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.AtomicExecutableConf
import org.apache.uima.jcas.JCas
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Root

/**
 *
 *
 * @autho Avner Maiberg (amaiberg@cs.cmu.edu)
 */

class ExecutionController[I, C <: ExecutableComponent[I]](explr: Explorer[CollectionReaderDescriptor, AtomicExecutableConf], exctr: Executor[I, _ <: ExecutableComponent[I]]) {

  def initializeCollectionReader(confRoot: Root[CollectionReaderDescriptor, AtomicExecutableConf]) = {
    val collectionReaderDesc = confRoot.root
    exctr.getComponentFactory.createReader(collectionReaderDesc)
  }

  def execute(confSpace: Root[CollectionReaderDescriptor, AtomicExecutableConf]): Unit = {

    val collectionReader = initializeCollectionReader(confSpace)
    val totalInputs = collectionReader.getTotalInputs
    lazy val confSpaceStream = explr.from(confSpace)

    def execStream(execStrm: Stream[explr.ElementWithHistory], inputNum: Int)(implicit cache: exctr.Cache = exctr.getEmptyCache(inputNum)): exctr.Cache =
      (execStrm, inputNum) match {
        case (Stream(), 0) =>
          println("done")
          cache
        case (Stream(), input) =>
          println("finished input: #" + input)
          execStream(confSpaceStream, input - 1)(cache ++ exctr.getEmptyCache(input - 1))
        case ((compDesc, hist) #:: rest, input) => {
          println("executing component (with history): " + compDesc)
          // println("with cache: " + cache)
          //println("getCachedResult: " + cache.dataCache(exctr.Trace(0,hist)))
          val (result, updatedCache) = exctr.execute(compDesc, exctr.Trace(input, hist))(cache)
          println("result:  " + result)
          //  println("resulting cache: " + updatedCache)
          //maybe do something with result here
          execStream(rest, input)(updatedCache)
        }
      }
    execStream(confSpaceStream, totalInputs - 1)
  }
}

