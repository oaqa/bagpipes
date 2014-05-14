package edu.cmu.lti.oaqa.bagpipes.pipeviz

import scala.util.matching.Regex

/**
 * The graph structure that we use to represent pipeline paths through
 * bagpipes.
 *
 * We can generalize this in the future.
 */

// TODO use IndexedSeq here instead of List

// For now, let's make the assumption that node names are unique and that is
// how we identify them.
class Node(nName : String, nLabel : String, isCollapse : Boolean = false) {
  private val collapsePrefix : String = "<COLLAPSED_NODES_"
  private val collapseSuffix : String = ">"

  val nodeName : String = isCollapse match {
    case false => nName
    case true  => collapsePrefix + nName + collapseSuffix
  }
  val nodeLabel : String = nLabel

  def isCollapseNode : Boolean = {
    val pattern : Regex = ("\\A" + collapsePrefix + ".+" + collapseSuffix + "\\z").r
    pattern.findFirstIn(nodeName) match {
      case None => false
      case Some(matchedStr) => true
    }
  }
}

class Edge(fNode : Node, tNode : Node) {
  val fromNode : Node = fNode
  val toNode : Node = tNode
}

class Cluster(cName : String, cNodes : List[Node]) {
  val clusterName : String = cName
  val clusterNodes : List[Node] = cNodes

  // When we collapse a Cluster, we keep only the union of the first `before`
  // nodes and the last `after` nodes. In between these two sections, if there
  // are any missing nodes, we replace them with a special Node with the node
  // name "<COLLAPSED_NODES_clusterName>".
  // So, the collapse node name has the prefix: "<COLLAPSED_NODES_", then the
  // `clusterName` value, then the suffix ">".
  def collapseCluster (before : Int, after : Int) : (Cluster, Set[Node], Option[Node]) = {
    val cNode = new Node(clusterName, "...", true)
    // Remove all nodes except for the `before` nodes and the `after` nodes.
    // Insert the node representing the collapse in between the before and
    // after parts.
    val (newNodes : List[Node], collapseNode : Option[Node]) =
      if (before + after < clusterNodes.length) {
        (clusterNodes.take(before) ++ (cNode
            +: clusterNodes.takeRight(after)),
          Some(cNode))
      }
      // The before and after parts cover the whole node list, so we don't do
      // anything to it.
      else {
        (clusterNodes, None)
      }
    // This will be empty if before and after overlap, but that is fine.
    val removedNodes : Set[Node] = clusterNodes.drop(before).dropRight(after).toSet

    (new Cluster(clusterName, newNodes), removedNodes, collapseNode)
  }
}

// A graph, for our purposes, is an ordered list of clusters and a set of edges.
// Each cluster has an ordered list of nodes, and the edge set connects nodes
// in between clusters, and for other purposes maybe within clusters.
class Graph(gClusters : List[Cluster], gEdges : Set[Edge]) {
  // Other constructor that accepts a list of edges and then converts them to
  // a set of edges.
  def this(gClusters : List[Cluster], gEdges : List[Edge]) = {
    this(gClusters, gEdges.toSet)
  }

  val clusters : List[Cluster] = gClusters
  val edges : Set[Edge] = gEdges.toSet

  // Given a list of edges, a set of nodes that have been collapsed, and the
  // node representing the collapsed nodes for a given cluster, we remove all
  // edges that are between two separate collapsed nodes, and redirect edges
  // between (preserved, collapsed) pairs. Other edges are left unchanged.
  private def collapseEdges (edges : Set[Edge], removeSet : Set[Node], collapseNode : Node) : Set[Edge] = {
    edges.foldRight (Set() : Set[Edge]) { case (edge : Edge, partEdges : Set[Edge]) =>
      (removeSet.contains(edge.fromNode), removeSet.contains(edge.toNode)) match {
        // Both nodes in the edge were collapsed, so don't bother drawing the edge
        case (true, true)   => partEdges
        // We preserve the edge from a node to a node that was collapsed
        case (true, false)  => (prependUncollapsed (new Edge(collapseNode,  edge.toNode))
                                                   (partEdges))
        case (false, true)  => (prependUncollapsed (new Edge(edge.fromNode, collapseNode))
                                                   (partEdges))
        // If neither node was collapsed, then we don't have to change anything
        case (false, false) => partEdges + edge
      }
    }
  }

  private def prependUncollapsed(edge : Edge) (edges : Set[Edge]) : Set[Edge] = {
    (edge.fromNode.isCollapseNode && edge.toNode.isCollapseNode) match {
      case true => edges
      case false => edges + edge
    }
  }

  private def collapseEdges (removeSet : Set[Node], collapseNode : Node) : Set[Edge] = {
    collapseEdges (edges, removeSet, collapseNode)
  }

  def collapseGraph (before : Int, after : Int) : Graph = {
    // Collapse all of the clusters and record the nodes that we have removed
    // by collapsing them. We also store a list of the nodes we use to
    // represent all collapsed nodes for each cluster.
    val base : (List[Cluster], List[Set[Node]], List[Node]) = (List(), List(), List())
    val (newClusters : List[Cluster], removedNodes : List[Set[Node]], collapseNodes : List[Node]) =
      clusters.foldRight (base) {
        case (nextCluster, (partClusters, partSets, partNodes)) =>
          val (c, s, n) = nextCluster.collapseCluster(before, after)
          // Join the newly processed cluster to the accumulated list of
          // processed clusters, union the sets of removed nodes,
          // and only add the collapse node if it was not `None`.
          (c +: partClusters,
           s.isEmpty match {
            case false => s +: partSets
            case true  => partSets
          },
           n match {
            case Some(node) => node +: partNodes
            case None       => partNodes
          })
        }
    // For each node that represents the collapsed node for a cluster
    // (i.e., for each cluster), we remove edges that exist between two
    // collapsed nodes, and preserve all other edges.
    val newEdges : Set[Edge] = collapseNodes.zip(removedNodes).foldLeft (edges) {
      case (newEdges : Set[Edge], (node : Node, removeSet : Set[Node])) =>
        collapseEdges (newEdges, removeSet, node)
    }

    new Graph (newClusters, newEdges)
  }

  def collapseGraph (numPreserve : Int) : Graph = {
    collapseGraph (numPreserve, numPreserve)
  }
}
