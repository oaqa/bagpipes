package edu.cmu.lti.oaqa.cse.scala.space
import edu.cmu.lti.oaqa.cse.scala.configuration.Descriptors._
import edu.cmu.lti.oaqa.cse.scala.configuration.Parameters.Parameter
import org.apache.uima.collection.CollectionReader
import edu.cmu.lti.oaqa.cse.scala.configuration.Parameters._

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
 */
class ConfigurationSpace(confDes: ConfigurationDescriptor) {
  import ConfigurationSpace._
  val confSpace = populateTree(confDes)
  val defaultExpanders: List[NodeExpander] = crossOptsExpander _ :: Nil
  def getSpace = space
  /*apply all the defaultExpanders to the tree and return the resulting configuration space*/
  lazy val space = defaultExpanders.foldLeft(confSpace)((space, expander) => expandNodes(expander, space))
}

object ConfigurationSpace {

  def apply(confDesc: ConfigurationDescriptor) = new ConfigurationSpace(confDesc)

  /**
   * n-ary tree to store the configuration space.
   */
  sealed abstract class Tree[T](elem: T) { def getElem = elem }
  case class Node[T](elem: T, children: Stream[Tree[T]] = Stream()) extends Tree[T](elem)
  case class Leaf[T](elem: T) extends Tree[T](elem)

  /**
   *  Returns a new [[$packagePath.ConfigurationSpace.Tree]] describing all possible
   *  execution paths given by a [[$packaPath.ConfigurationDescripor]].
   *  @param confDes
   *    The configuration descriptor describing the `ConfigurationSpace`.
   *
   */
  private def populateTree(confDes: ConfigurationDescriptor): Tree[ExecutableConf] = {
    /**
     * INNER FUNCTION:
     * Returns the children of the root node (expanding the entire discrete version
     * of the tree).
     *
     * Iterates over arbitrary list of ExecutableDescriptor, expanding
     * [[$packagePath.PhaseDescriptor]] to its tree representation.
     */
    def populateTree(execs: List[ExecutableConf]): Stream[Tree[ExecutableConf]] = execs match {
      //(1) pipeline is empty, should consider failing on this.
      case Nil => Stream()
      //(2) last element is a phase, expand phase to its list of options as new leaves. 
      //(reached end of pipeline)
      case PhaseDescriptor(_, options) :: Nil => options.toStream.map(Leaf[ExecutableConf](_))
      //(3) last element is a component, return component in a new leaf. (reached end of pipeline)
      case head :: Nil => Stream(Leaf(head))
      //(4) element is a phase, expand phase to its list of options, 
      //recursively populating the rest  of the tree, and setting the resulting 
      //tree(s) as the children of each option in the phase
      case PhaseDescriptor(_, options) :: tail => options.toStream.map(Node(_, populateTree(tail)))
      //(5) element is a component, store standalone component in node, 
      // recursively populating the rest of the tree setting the resulting 
      // tree(s) as the children of the node
      case head :: tail => Stream(Node(head, populateTree(tail)))
    }

    //begin populating tree by expanding the root node containing the collection-reader.
    confDes match {
      case ConfigurationDescriptor(_, collectionReader, pipeline) => Node[ExecutableConf](collectionReader, populateTree(pipeline))
    }
  }

  type NodeExpander = ExecutableConf => Stream[ExecutableConf]

  /**
   * Applies a [[NodeExpander]] to each of the nodes in the configuration space and returns the resulting tree.
   *
   * @param expand the [[NodeExpander]] to be applied to the nodes.
   * @return the expanded space.
   */
  def expandNodes(expand: NodeExpander, confSpace: Tree[ExecutableConf]): Tree[ExecutableConf] = {
    /*since expand returns a list of components, wrap the component in a leaf or a node*/
    def expandNode(expand: NodeExpander, tree: Tree[ExecutableConf]) = tree match {
      case Leaf(elem) => expand(elem).map(Leaf(_))
      case Node(elem, children) => expand(elem).map(Node(_, children))
    }

    /*perform a depth-first search of the tree applying expand to all of the nodes*/
    def expandNodes(expand: NodeExpander, confSpace: Tree[ExecutableConf]): Tree[ExecutableConf] = confSpace match {
      case Leaf(elem) => Leaf(elem)
      case Node(elem, children) => Node(elem, children.flatMap(expandNode(expand, _)).map(expandNodes(expand, _)))
    }

    expandNodes(expand, confSpace)
  }

  /**
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
      case CrossComponentDescriptor(name, params, crossParams) => for (pMap <- crossParamsExpander(crossParams)) yield ComponentDescriptor(name, params ++ pMap)
      case other => Stream(other)
    }
  }

}