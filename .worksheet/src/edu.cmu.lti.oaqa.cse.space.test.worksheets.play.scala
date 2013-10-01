package edu.cmu.lti.oaqa.cse.space.test.worksheets
import net.liftweb.json.JsonAST._
import net.liftweb.json.Extraction._
import net.liftweb.json.Printer._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import net.liftweb.json.parse
import net.liftweb.json.DefaultFormats
import net.liftweb.json.{ Serialization, ShortTypeHints }
import scala.util.parsing.json.JSONObject
import org.junit.runner.RunWith
import org.scalatest.FeatureSpec
import org.junit._
import org.scalatest.junit.JUnitRunner
import scala.reflect.ClassManifest
import edu.cmu.lti.oaqa.bagpipes.configuration.YAMLParser
import edu.cmu.lti.oaqa.bagpipes.configuration.Parser
import edu.cmu.lti.oaqa.bagpipes.configuration._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors._
import edu.cmu.lti.oaqa.bagpipes.configuration.Implicits._
import edu.cmu.lti.oaqa.bagpipes.space.test._
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._
object play {
  type TreeWithHistory = (Tree[ExecutableConf], List[Leaf[ExecutableConf]])
  trait Ordering
  object Depth extends Ordering
  object Breadth extends Ordering;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(1792); 
  def from(initial: Tree[ExecutableConf], ord: Ordering = Breadth) = {
    val ordering = ord
    def from(initial: Stream[TreeWithHistory]): Stream[TreeWithHistory] = initial match {
      case Stream() => Stream()
      case cur @ (Leaf(e), hist) #:: rest => cur #::: from(rest)
      case cur @ (Node(e, children), hist) #:: rest => {
        val more = for (c <- children) yield { (c, hist ::: List(Leaf(e))) }
        cur #::: {
          ord match {
            case Depth => from(more) #::: from(rest) //depth-first
            case Breadth => from(rest) #::: from(more) //breadth-first
          }
        }
      }
    }
    from(Stream((initial, Nil)))
  };System.out.println("""from: (initial: edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Tree[edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf], ord: edu.cmu.lti.oaqa.cse.space.test.worksheets.play.Ordering)Stream[(edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Tree[edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf], List[edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Leaf[edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf]])]""");$skip(107); 

  def isLeaf[T](tree: Tree[T]) =
    tree match {
      case Leaf(_) => true
      case _ => false
    };System.out.println("""isLeaf: [T](tree: edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Tree[T])Boolean""");$skip(320); val res$0 = 
  new confTrees {
    lazy val pathsFromStart = from(confTree5)
    //   lazy val pathsToGoal: Stream[List[Tree[ExecutableConf]]] = pathsFromStart.filter(tree => isLeaf(tree._1)).map((t: TreeWithHistory) => t._2 :: List[Tree[ExecutableConf]](t._1))
    // println(pathsToGoal.map(_.map(_.getElem())).take(3).toList)
  };System.out.println("""res0: edu.cmu.lti.oaqa.bagpipes.space.test.confTrees{lazy val pathsFromStart: Stream[(edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Tree[edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf], List[edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Leaf[edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf]])]} = """ + $show(res$0))}
}
