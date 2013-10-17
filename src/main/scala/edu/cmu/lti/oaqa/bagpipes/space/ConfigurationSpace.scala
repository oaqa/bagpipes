package edu.cmu.lti.oaqa.bagpipes.space
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf._
import edu.cmu.lti.oaqa.bagpipes.configuration.Parameters.Parameter
import org.apache.uima.collection.CollectionReader
import edu.cmu.lti.oaqa.bagpipes.configuration.Parameters._

/**
 * Provides mapping between configuration descriptor to configuration space.
 *    *      *
 * Example:
 * The following configuration descriptor,
 * {{{
 * (<-----------------ConfigurationDescriptor----------------->)
 *                    (<------Phases----->)
 * collection-reader   p_0     p_1     p_2     standalone
 * -----------------  ------  ------  ------  -----------
 * 		                      c_1_0
 *                                    c_2_0
 * 		   cr         c_0_0   c_1_1               s_0
 *                                    c_2_1
 * 		                      c_1_2
 * }}}
 *
 *
 * results in,
 *
 * {{{
 * collection-reader:         cr
 *                            +
 * p_0:                     c_0_0
 *             +--------------+--------------+
 * p_1:      c_1_0          c_1_1          c_1_2
 *         +------+       +------+       +------+
 * p_2:  c_2_0  c_2_1   c_2_0  c_2_1   c_2_0  c_2_1
 *         +      +       +      +       +       +
 * std:  s_3_0  s_3_0   s_3_0  s_3_0   s_3_0   s_3_0
 * }}}
 *
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */
class ConfigurationSpace(confDes: ConfigurationDescriptor) {
  import ConfigurationSpace._
  val confSpace = populateTree(confDes)
  val defaultExpanders: List[NodeExpander] = crossOptsExpander _ :: Nil
  lazy val space = defaultExpanders.foldLeft(confSpace)((space, expander) => expandNodes(expander, space))

  def getSpace = space
  /*apply all the defaultExpanders to the tree and return the resulting configuration space*/
}

object ConfigurationSpace {

  def apply(confDesc: ConfigurationDescriptor) = new ConfigurationSpace(confDesc)

  /**
   * n-ary tree to store the configuration space.
   */

  //top-level class for all nodes
  sealed abstract class Tree[T](elem: T) { def getElem = elem }
  //top-level class for nodes containing children
  sealed abstract class TreeWithChildren[T](elem: T, children: Stream[Tree[T]]) extends Tree(elem) {
    def getChildren = children
  }
  //used for pattern-matching
  object TreeWithChildren {
    def unapply[T](tree: TreeWithChildren[T]): Option[(T, Stream[Tree[T]])] = Some(tree.getElem, tree.getChildren)
  }
  //sealed abstract class NodeWithChildren[E, T](elem: E, children: Stream[T]) extends Tree[E](elem)
  case class Node[T](elem: T, children: Stream[Tree[T]] = Stream()) extends TreeWithChildren[T](elem, children)
  case class Leaf[T](elem: T) extends Tree[T](elem)
  //Entry-point to the tree that also contains a special element
  case class Root[R <: T, T](root: R, trees: Stream[Tree[T]] = Stream()) extends TreeWithChildren[T](root, trees)
  /**
   *  Returns a new [[$packagePath.ConfigurationSpace.Tree]] describing all possible
   *  execution paths given by a [[$packaPath.ConfigurationDescripor]].
   *  @param confDes
   *    The configuration descriptor describing the `ConfigurationSpace`.
   *
   */
  private def populateTree(confDes: ConfigurationDescriptor): Root[CollectionReaderDescriptor, ExecutableConf] = {
    /**
     * INNER FUNCTION:
     * Returns the children of the root node (expanding the entire discrete version
     * of the tree).
     *
     * Iterates over arbitrary list of ExecutableDescriptor, expanding
     * [[$packagePath.PhaseDescriptor]] to its tree representation.
     */
    def populateTree(execs: List[PipelineDescriptor]): Stream[Tree[ExecutableConf]] = execs match {
      //(1) pipeline is empty, should consider failing on this.
      case Nil => Stream()
      //(2) last element is a phase, expand phase to its list of options as new leaves. 
      //(reached end of pipeline)
      case PhaseDescriptor(_, options) :: Nil => options.toStream.map(Leaf[ExecutableConf](_))
      //(3) last element is a component, return component in a new leaf. (reached end of pipeline)
      case (head @ ExecutableConf(_, _)) :: Nil => Stream(Leaf(head))
      //(4) element is a phase, expand phase to its list of options. 
      //Recursively populate the rest  of the tree, and setting the resulting 
      //tree(s) as the children of each option in the phase
      case PhaseDescriptor(_, options) :: tail => options.toStream.map(Node(_, populateTree(tail)))
      //(5) element is a component, store standalone component in a node. 
      // Recursively populate the rest of the tree setting the resulting  
      // tree(s) as the children of the node
      case (head @ ExecutableConf(_, _)) :: tail => Stream(Node(head, populateTree(tail)))
    }

    //begin populating tree by expanding the root node containing the collection-reader.
    confDes match {
      case ConfigurationDescriptor(_, collectionReader, pipeline) => Root[CollectionReaderDescriptor, ExecutableConf](collectionReader, populateTree(pipeline))
    }
  }

  type NodeExpander = ExecutableConf => Stream[ExecutableConf]

  /**
   * Applies a [[NodeExpander]] to each of the nodes in the configuration space
   * and returns the resulting tree.
   *
   * @param expand the [[NodeExpander]] to be applied to the nodes.
   * @return the expanded space.
   */
  def expandNodes(expand: NodeExpander, confSpace: Root[CollectionReaderDescriptor, ExecutableConf]): Root[CollectionReaderDescriptor, ExecutableConf] = {
    /*since expand returns a list of components, wrap the component in a leaf or a node*/
    def expandNode(expand: NodeExpander, tree: Tree[ExecutableConf]) = tree match {
      case Leaf(elem) => expand(elem).map(Leaf(_))
      case TreeWithChildren(elem, children) => expand(elem).map(Node(_, children))
    }

    /*perform a depth-first traversal of the tree applying expand to all of the nodes*/
    def expandNodes(expand: NodeExpander, confSpace: Tree[ExecutableConf]): Tree[ExecutableConf] = confSpace match {
      case Leaf(elem) => Leaf(elem)
      case TreeWithChildren(elem, children) => Node(elem, children.flatMap(expandNode(expand, _)).map(expandNodes(expand, _)))
    }
    
    Root(confSpace.root, confSpace.trees.flatMap(expandNode(expand, _)).map(expandNodes(expand, _)))
  }

  /**
   *
   *
   */
  def crossOptsExpander[K, V](elem: ExecutableConf): Stream[ExecutableConf] = {
    def crossParamsExpander[K, V](crossParams: Map[K, List[V]]): Stream[Map[K, V]] = crossParams.keys.toList match {
      case Nil => Stream(Map())
      case headKey :: tailKeys => {
        for {
          p <- crossParams(headKey)
          pMap <- crossParamsExpander(crossParams.tail.toMap)
        } yield Map(headKey -> p) ++ pMap
      }.toStream
    }
    elem match {
      case CrossComponentDescriptor(name, params, crossParams) => for (pMap <- crossParamsExpander[String, Parameter](crossParams.flatMap { case (k, v) => Map(k -> v.pList) })) yield ComponentDescriptor(name, params ++ pMap)
      case other => Stream(other)
    }
  }

}