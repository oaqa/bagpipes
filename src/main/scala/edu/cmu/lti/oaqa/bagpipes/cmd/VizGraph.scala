package edu.cmu.lti.oaqa.bagpipes.cmd

/**
 * The graph structure that we use to represent pipeline paths through
 * bagpipes.
 *
 * We can generalize this in the future.
 */

class Node(nName : String, nLabel : String) {
  val nodeName : String = nName
  val nodeLabel : String = nLabel
}

class Edge(fNode : Node, tNode : Node) {
  val fromNode : Node = fNode
  val toNode : Node = tNode
}

class Cluster(cName : String, cNodes : List[Node]) {
  val clusterName : String = cName
  var clusterNodes : List[Node] = cNodes
}

class Graph(gClusters : List[Cluster], gEdges : List[Edge]) {
  var clusters : List[Cluster] = gClusters
  var edges : List[Edge] = gEdges
}