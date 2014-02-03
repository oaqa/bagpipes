package edu.cmu.lti.oaqa.bagpipes.execution.test
import edu.cmu.lti.oaqa.bagpipes.space.explorer.Explorer
import edu.cmu.lti.oaqa.bagpipes.executor.Executor
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutableComponent
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutorTypes
import edu.cmu.lti.oaqa.bagpipes.executor._
import edu.cmu.lti.oaqa.bagpipes.space._
import edu.cmu.lti.oaqa.bagpipes.space.explorer.SimpleExplorer._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ConfigurationDescriptor
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.space.test._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FeatureSpec
import edu.cmu.lti.oaqa.bagpipes.controller.ExecutionController
import edu.cmu.lti.oaqa.bagpipes.executor.uima.UimaExecutor
import edu.cmu.lti.oaqa.bagpipes.space.explorer.DepthExplorer
import edu.cmu.lti.oaqa.bagpipes.space.explorer.BreadthExplorer
import edu.cmu.lti.oaqa.bagpipes.space.explorer.KBestPathExplorer

@RunWith(classOf[JUnitRunner])
class ExecutorSpec extends FeatureSpec {
  feature("Simple detph-first executor") {
    new confTrees {
      val controller = SimpleDepthExecutionController
      scenario("executing confTree4") {
     //  controller.execute(confTree4)
      }
    }
  }

  feature("Simple breadth-first explorer") {
    new confTrees {

      val controller = SimpleBreadthExecutionController
      scenario("executing confTree4") {
        //   controller.execute(confTree4)
      }
    }
  }

  feature("Uima depth-first explorer") {
    new confTrees {
      val controller = SimpleUimaExecutionController
      scenario("executing confTree4") {
        //  controller.execute(confTree4)
      }
    }
  }

  feature("Simple KBestPath explorer") {
    new confTrees with kBest {
      val controller1 = ExecutionController(KBestPathExplorer(TestScorer1), UimaExecutor)
      val controller2 = ExecutionController(KBestPathExplorer(TestScorer2), UimaExecutor)
      scenario("executing confTree4 with TestScorer1") {
           controller1.execute(confTree4)
      }
      scenario("executing confTree4 with TestScorer2") {
        //  controller2.execute(confTree4)
      }
    }
  }
}

object SimpleDepthExecutionController extends ExecutionController(DepthExplorer, SimpleExecutor)
object SimpleBreadthExecutionController extends ExecutionController(BreadthExplorer, SimpleExecutor)
object SimpleUimaExecutionController extends ExecutionController(DepthExplorer, UimaExecutor)
