package edu.cmu.lti.oaqa.bagpipes.space
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf
import SimpleExplorer._

/**
 * Provides some traversal over a configuration space tree in the form of a [[Stream]].
 *
 * Abstract method `from` provides the stream of nodes given by the traversal strategy
 * of this [[Explorer]].
 *
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

trait Explorer[T] extends HistoryTypes[T] {
  def from(initial: Tree[T]): Stream[ElementWithHistory]
}

/**
 * Convenience types for retaining the trace "history" for each of the explored
 * nodes. Implicit methods help convert from nodes to leaves for simplicity's
 * sake.
 */
trait HistoryTypes[T] {
  type TreeWithHistory = (Tree[T], Stream[Leaf[T]])
  type LeafWithHistory = (Leaf[T], Stream[Leaf[T]])
  type NodeWithHistory = (Node[T], Stream[Leaf[T]])
  type ElementWithHistory = (T, Stream[T])
  implicit def nodeWithHistory2ElementWithHistory[T](node: Stream[TreeWithHistory]): Stream[ElementWithHistory] = node match {
    case cur @ (Leaf(element), hist) #:: rest => Stream((element, hist.map(_.elem)))
    case cur @ (Node(element, children), hist) #:: rest => Stream((element, hist.map(_.elem)))
  }
}

