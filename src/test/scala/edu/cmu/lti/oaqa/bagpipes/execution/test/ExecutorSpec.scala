package edu.cmu.lti.oaqa.bagpipes.execution.test
import edu.cmu.lti.oaqa.bagpipes.space.Explorer
import edu.cmu.lti.oaqa.bagpipes.executor.Executor
import edu.cmu.lti.oaqa.bagpipes.executor.DataCache
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutableComponent
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutorTypes
import edu.cmu.lti.oaqa.bagpipes.executor._
import edu.cmu.lti.oaqa.bagpipes.space._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ConfigurationDescriptor
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ComponentDescriptor
import edu.cmu.lti.oaqa.bagpipes.space.test._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FeatureSpec
import edu.cmu.lti.oaqa.bagpipes.controller.ExecutionController
import edu.cmu.lti.oaqa.bagpipes.executor.uima.UimaExecutor

@RunWith(classOf[JUnitRunner])
class ExecutorSpec extends FeatureSpec {
  feature("Simple detph-first executor") {
    new confTrees {
      val controller = SimpleDepthExecutionController
      scenario("executing confTree4") {
  //      controller.execute(confTree4)
      }
    }
  }

  feature("Simple breadth-first explorer") {
    new confTrees {
      val controller = SimpleBreadthExecutionController
      scenario("executing confTree4") {
 //       controller.execute(confTree4)
      }
    }
  }

  feature("Uima depth-first explorer") {
    new confTrees {
      val controller = SimpleUimaExecutionController
      scenario("executing confTree4") {
    	  controller.execute(confTree4)
      }
    }
  }
}

object SimpleDepthExecutionController extends ExecutionController(DepthExplorer, SimpleExecutor)
object SimpleBreadthExecutionController extends ExecutionController(BreadthExplorer, SimpleExecutor)
object SimpleUimaExecutionController extends ExecutionController(DepthExplorer, UimaExecutor)