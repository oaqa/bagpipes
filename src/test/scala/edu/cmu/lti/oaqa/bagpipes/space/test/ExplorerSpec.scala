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
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.space.explorer.KBestPathExplorer
import edu.cmu.lti.oaqa.bagpipes.space.explorer.DepthExplorer
import edu.cmu.lti.oaqa.bagpipes.space.explorer.BreadthExplorer

@RunWith(classOf[JUnitRunner])
class ExplorerSpec extends FeatureSpec with HistoryTypes[AtomicExecutableConf] {
  def tree2Tup(tree: TreeWithHistory[_]) = TreeWithHistory.unapply(tree).get
  def assertEquals(x: Stream[(_, _)], y: Stream[TreeWithHistory[_]]) =
    assert(x === y.map(tree2Tup(_)))

  implicit val input = 0
  feature("Simple depth-first exploration") {
    new depthFirst with confTrees {

      scenario("confTree0") {
        assertEquals(exploredConfTree0, DepthExplorer.fromRoot(confTree0))
      }
      scenario("confTree1") {
        assertEquals(exploredConfTree1, DepthExplorer.fromRoot(confTree1))
      }
      scenario("confTree2") {
        assertEquals(exploredConfTree2, DepthExplorer.fromRoot(confTree2))
      }
      scenario("confTree3") {
        assertEquals(exploredConfTree3, DepthExplorer.fromRoot(confTree3))
      }
      scenario("confTree4") {
        assertEquals(exploredConfTree4, DepthExplorer.fromRoot(confTree4))
      }
    }
  }
  feature("Simple breadth-first exploration") {
    new breadthFirst with confTrees {
      scenario("confTree0") {
        assertEquals(exploredConfTree0, BreadthExplorer.fromRoot(confTree0))
      }
      scenario("confTree1") {
        assertEquals(exploredConfTree1, BreadthExplorer.fromRoot(confTree1))
      }
      scenario("confTree2") {
        assertEquals(exploredConfTree2, BreadthExplorer.fromRoot(confTree2))
      }
      scenario("confTree3") {
        assertEquals(exploredConfTree3, BreadthExplorer.fromRoot(confTree3))
      } 
      scenario("confTree4") {
        assertEquals(exploredConfTree4, BreadthExplorer.fromRoot(confTree4))
      }
    }
  }

  feature("KBestPath Exploration") {
    new kBest with confTrees {
      scenario("confTree4: scorer 1") {
        assertEquals(exploredConfTree4_scorer1, KBestPathExplorer(TestScorer1).fromRoot(confTree4))
      }
      scenario("confTree4: scorer 2") {
        assertEquals(exploredConfTree4_scorer2, KBestPathExplorer(TestScorer2).fromRoot(confTree4))
      }
    }
  }
}