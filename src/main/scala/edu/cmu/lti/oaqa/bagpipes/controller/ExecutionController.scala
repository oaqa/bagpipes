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

/**
 * 
 *
 * @autho Avner Maiberg (amaiberg@cs.cmu.edu)
 */

class ExecutionController[I, C <: ExecutableComponent[I]](explr: Explorer[AtomicExecutableConf], exctr: Executor[I, _ <: ExecutableComponent[I]]) {

 

  def execute(confSpace: Tree[AtomicExecutableConf])(implicit inputNum: Int = 0): Unit = {
    val executionStream = explr.from(confSpace)
    def execStream(execStrm: Stream[explr.ElementWithHistory])(cache: exctr.Cache = exctr.getEmptyCache): exctr.Cache = execStrm match {
      case Stream() => { println("Done"); cache }
      case (compDesc, hist) #:: rest => {
        println("executing component (with history): " + compDesc)
        //  println("with cache: " + cache)
        //println("getCachedResult: " + cache.dataCache(exctr.Trace(0,hist)))
        val (result, updatedCache) = exctr.execute(compDesc, exctr.Trace(inputNum, hist))(cache)
        println("result:  " + result)
        //  println("resulting cache: " + updatedCache)
        //maybe do something with result here
        execStream(rest)(updatedCache)
      }
    }
    execStream(executionStream)()
  }
}

