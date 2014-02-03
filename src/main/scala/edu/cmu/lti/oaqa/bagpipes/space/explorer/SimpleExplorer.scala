package edu.cmu.lti.oaqa.bagpipes.space.explorer
import SimpleExplorer._
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.space.Leaf
import edu.cmu.lti.oaqa.bagpipes.space.TreeWithChildren
import scala.collection.immutable.Stream.consWrapper

/**
 * SimpleExplorer provides a depth/breadth-first (given by the specified
 * `Ordering`) traversal of all the nodes in a configuration space.
 *
 * See `BreadthFirstExplorer` and `DepthFirstExplorer` singletons.
 *
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

protected class SimpleExplorer[R <: T, T, I](order: Ordering) extends Explorer[R, T, I] {
  /**
   * Returns either a depth-first or breadth-first ordering of the configuration
   * space as specified by `Ordering`.
   *
   * @oaram initial
   * 			The initial position, or root in the configuration space tree.
   */

  override def from(initial: Stream[ExecutableTree])(implicit input: I): Stream[ExecutableTree] = order match {
    case Depth => fromDepth(initial) //depth-first
    case Breadth => fromBreadth(initial) //breadth-first
  }

  /**
   * Returns a stream of [[edu.cmu.lti.oaqa.bagpipes.space.TreeWithHistory]]
   * using depth-first search traversal.
   *
   * @oaram initial
   * 			The initial position (or root) in the configuration space tree.
   */
  private def fromDepth(initial: Stream[ExecutableTree]): Stream[ExecutableTree] = initial match {
    case Stream() => Stream() // no more nodes, terminate
    case (current @ Leaf(_, _)) #:: siblings => current #:: fromDepth(siblings) // leaf encountered, visit current, and go to next sibling  
    case (current @ TreeWithChildren(_, children, _)) #:: siblings => // node encountered, 
      current #:: fromDepth(children) #::: fromDepth(siblings) //visit current, then visit its children, and then visit its siblings
  }
  /**
   * Returns a stream of [[edu.cmu.lti.oaqa.bagpipes.space.TreeWithHistory]]
   * using breadth-first search traversal.
   *
   * @oaram initial
   * 			the initial position (or root) in the configuration space tree.
   */
  private def fromBreadth(initial: Stream[ExecutableTree]): Stream[ExecutableTree] = {
    def fromBreadth(initial: Stream[ExecutableTree], childrenAcc: Stream[ExecutableTree]): Stream[ExecutableTree] =
      (initial, childrenAcc) match {
        case (Stream(), Stream()) => Stream() // no more children for breadth-first search, terminate
        case (Stream(), childrenAcc) => fromBreadth(childrenAcc, Stream()) // finished level of breadth-first, move on to next
        case ((current @ Leaf(_, _)) #:: siblings, _) => current #:: fromBreadth(siblings, Stream()) // leaf encountered, visit leaf and move on to next sibling
        case ((current @ TreeWithChildren(_, children, _)) #:: siblings, _) => // node encountered, visit node 
          current #:: fromBreadth(siblings, childrenAcc #::: children) //visit node, go to next sibling, and remember children for next breadth-first level
      }
    fromBreadth(initial, Stream())
  }

}

/*Explorer singletons*/
object BreadthExplorer extends SimpleExplorer[CollectionReaderDescriptor, AtomicExecutableConf, Int](SimpleExplorer.Breadth)
object DepthExplorer extends SimpleExplorer[CollectionReaderDescriptor, AtomicExecutableConf, Int](SimpleExplorer.Depth)

/*Enums for selecting either depth-first or breadth-first search*/
object SimpleExplorer {
  sealed trait Ordering
  object Depth extends Ordering
  object Breadth extends Ordering
}