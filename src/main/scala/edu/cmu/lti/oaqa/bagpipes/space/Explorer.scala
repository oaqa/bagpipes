package edu.cmu.lti.oaqa.bagpipes.space
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf
import SimpleExplorer._

/**
 * Provides some traversal over a configuration space tree in the form of a [[Stream]] 
 */

trait Explorer[T] extends HistoryTypes[T] {
  def from(initial: Tree[T]): Stream[LeafWithHistory]
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
  implicit def nodeWithHistory2LeafWithHistory[T](node: Stream[TreeWithHistory]): Stream[LeafWithHistory] = node match {
    case cur @ (Leaf(element), hist) #:: rest => Stream((Leaf(element), hist))
    case cur @ (Node(element, children), hist) #:: rest => Stream((Leaf(element), hist))
  }
}

