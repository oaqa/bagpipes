package edu.cmu.lti.oaqa.bagpipes.space.test
import org.junit.runner.RunWith
import org.scalatest.FeatureSpec
import org.junit._
import org.scalatest.junit.JUnitRunner
import edu.cmu.lti.oaqa.bagpipes.configuration._
import edu.cmu.lti.oaqa.bagpipes.configuration.Implicits._
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._
import edu.cmu.lti.oaqa.bagpipes.space._

@RunWith(classOf[JUnitRunner])
class ExplorerSpec extends FeatureSpec {
  feature("Simple depth-first exploration") {
    new depthFirst with confTrees {
      scenario("confTree0") {
        assert(exploredConfTree0 === DepthExplorer.from(confTree0))
      }
      scenario("confTree1") {
        assert(exploredConfTree1 === DepthExplorer.from(confTree1))
      }
      scenario("confTree2") {
        assert(exploredConfTree2 === DepthExplorer.from(confTree2))
      }
      scenario("confTree3") {
        assert(exploredConfTree3 === DepthExplorer.from(confTree3))
      }
      scenario("confTree4") {
        assert(exploredConfTree4 === DepthExplorer.from(confTree4))
      }
    }
  }
  feature("Simple breadth-first exploration") {
    new breadthFirst with confTrees {
      scenario("confTree0") {
        assert(exploredConfTree0 === BreadthExplorer.from(confTree0))
      }
      scenario("confTree1") {
        assert(exploredConfTree1 === BreadthExplorer.from(confTree1))
      }
      scenario("confTree2") {
        assert(exploredConfTree2 === BreadthExplorer.from(confTree2))
      }
      scenario("confTree3") {
        assert(exploredConfTree3 === BreadthExplorer.from(confTree3))
      }
      scenario("confTree4") {
        assert(exploredConfTree4 === BreadthExplorer.from(confTree4))
      }
    }
  }
}