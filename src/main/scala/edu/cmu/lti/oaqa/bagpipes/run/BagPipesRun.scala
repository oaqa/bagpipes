package edu.cmu.lti.oaqa.bagpipes.run
import edu.cmu.lti.oaqa.bagpipes.space.explorer.Explorer
import edu.cmu.lti.oaqa.bagpipes.space.explorer.SimpleExplorer._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ConfigurationDescriptor
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.controller.ExecutionController
import edu.cmu.lti.oaqa.bagpipes.executor.uima.UimaExecutor
import edu.cmu.lti.oaqa.bagpipes.space.explorer.DepthExplorer
import edu.cmu.lti.oaqa.bagpipes.space.explorer.BreadthExplorer
import edu.cmu.lti.oaqa.bagpipes.space.explorer.KBestPathExplorer
import edu.cmu.lti.oaqa.bagpipes.configuration.YAMLParser

object BagPipesRun {
  //pass some of these as arguments or in the yaml descriptor
  //Also, make explorer and executor parameters
  def run(args: Array[String]): Unit = {
    val controller = SimpleUimaExecutionController
    val parser = YAMLParser
    //get configuration descriptor path
    val path = args.head
    //parse to ConfigurationDescriptor object
    val confDesc = parser.parse(path, true)
    //ConfigurationDescriptor -> ConfigurationSpace
    val spaceTree = ConfigurationSpace(confDesc).getSpace
    //execute pipeline
    controller.execute(spaceTree)
  }
}

object SimpleUimaExecutionController extends ExecutionController(DepthExplorer, UimaExecutor)
