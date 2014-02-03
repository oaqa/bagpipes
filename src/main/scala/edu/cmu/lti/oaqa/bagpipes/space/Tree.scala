package edu.cmu.lti.oaqa.bagpipes.space

/**
 * n-ary tree to store the configuration space.
 */

//top-level class for all nodes
sealed abstract class Tree[T](elem: T) extends HistoryTypes[T] { def getElem = elem }

//top-level class for nodes containing history (leaf or nodes)
sealed abstract class TreeWithHistory[T](elem: T, history: Stream[T]) extends Tree[T](elem) {
  def getHistory = history
}

//used for pattern-matching
object TreeWithHistory {
  def unapply[T](tree: TreeWithHistory[T]): Option[(T, Stream[T])] = Some((tree.getElem, tree.getHistory))
}

//top-level class for nodes containing children (root or node)
sealed abstract class TreeWithChildren[T](elem: T, children: Stream[TreeWithHistory[T]], hist: Stream[T]) extends TreeWithHistory[T](elem, hist) {
  def getChildren = children
}

//used for pattern-matching
object TreeWithChildren {
  def unapply[T](tree: TreeWithChildren[T]): Option[(T, Stream[TreeWithHistory[T]], Stream[T])] = Some(tree.getElem, tree.getChildren, tree.getHistory)
}

sealed trait Child

//Entry-point to the tree that also contains a special "root" element
case class Root[R <: T, T](root: R, trees: Stream[TreeWithHistory[T] with Child] = Stream()) extends TreeWithChildren[T](root, trees, Stream())
case class Node[T](elem: T, children: Stream[TreeWithHistory[T]], hist: Stream[T]) extends TreeWithChildren[T](elem, children, hist) with Child
case class Leaf[T](elem: T, hist: Stream[T]) extends TreeWithHistory[T](elem, hist) with Child

/**
 * Convenience types for retaining the trace "history" for each of the explored
 * nodes. Implicit methods help convert from nodes to leaves for simplicity's
 * sake.
 */
trait HistoryTypes[T] {
  // type TreeWithHistory = (Tree[T], Stream[Leaf[T]])
  //type LeafWithHistory = (Leaf[T], Stream[Leaf[T]])
  //type NodeWithHistory = (Node[T], Stream[Leaf[T]])
  type ElementWithHistory = (T, Stream[T])
  /*
  implicit def nodeWithHistory2ElementWithHistory[T](node: Stream[TreeWithHistory]): Stream[ElementWithHistory] = node match {
    case cur @ Leaf(element, hist) #:: rest => Stream((element, hist.map(_.elem)))
    case cur @ (TreeWithChildren(element, children), hist) #:: rest => Stream((element, hist.map(_.elem)))
  }*/
}
