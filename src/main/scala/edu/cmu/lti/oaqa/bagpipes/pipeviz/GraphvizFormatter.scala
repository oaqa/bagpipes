package edu.cmu.lti.oaqa.bagpipes.pipeviz

/**
 * The output format for Graphviz.
 * As a reminder, to get the ball rolling and output the whole graph as a
 * formatted Graphviz string, use the method `formatGraph`.
 */

class GraphvizFormatter (vizArg : Viz) extends VizOutputFormat {
  val viz : Viz = vizArg

  val shapeStrs = Vector("box", "ellipse", "rectangle", "diamond",
      "parallelogram", "house", "hexagon", "trapezium", "egg",
      "circle", "oval")

  def formatCluster (x : (Int, Cluster)) : String = {
    val (clusterNo, cluster) = x
    val clusterShape : NodeShape = availableShape(clusterNo)

    val clusterLabel = "label=\"" + cluster.clusterName + "\";"
    // A list of node declarations along with the node parameters
    val nodeParams : List[String] =
      cluster.clusterNodes.map (formatNode (clusterShape) (_))

    // We create a subgraph section. This includes the section header, a
    // subgraph cluster label, and the list of nodes within that subgraph
    ((makeIndent (4) (1)) + "subgraph cluster_" + clusterNo.toString() + " {\n"
        + (makeIndent (4) (2)) + clusterLabel + "\n"
        + (makeIndent (4) (2)) + nodeParams.mkString("\n" + (makeIndent (4) (2)))
        + "\n" + (makeIndent (4) (2)) + (cluster.clusterNodes.map(x => x.nodeName)).mkString(" -> ") + " [style=\"invis\"];"
        + "\n" + (makeIndent (4) (1)) + "}")
  }

  def formatShape (shape : NodeShape) : String = {
    shape.shape()
  }

  def formatNode (shape : NodeShape) (node : Node) : String = {
    val parameters = "[label=\"" + node.nodeLabel + "\", shape=" + formatShape(shape) + "];"
    node.nodeName + " " + parameters
  }

  // Converts the graph edge to the output format expected by graphviz
  def formatEdge (edge : Edge) : String = {
    edge.fromNode.nodeName + " -> {" + edge.toNode.nodeName + "};"
  }

  def joinClustersAndEdges (clusters : IndexedSeq[String]) (edges : IndexedSeq[String]) : String = {
    ("digraph {\n"
        + (makeIndent (4) (1)) + "rankdir=LR;\n\n"
        + clusters.mkString("\n") + "\n\n"
        + (makeIndent (4) (1)) + edges.mkString("\n" + (makeIndent (4) (1)))
        + "\n}")
  }

  // TODO implement this feature
  def formatTree (tree : TreeGraph) : String = {
    ""
  }
}
