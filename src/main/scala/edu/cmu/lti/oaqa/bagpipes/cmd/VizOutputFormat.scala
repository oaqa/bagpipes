package edu.cmu.lti.oaqa.bagpipes.cmd

/**
 * A shape for node output.
 * We don't define this in VizGraph.scala because it represents formatting
 * information, not actual graph information.
 */
class NodeShape(shapeStr : String) {
  def shape() = shapeStr
}

/**
 * A trait specifying an output format (output to text) for a VizGraph object.
 */

trait VizOutputFormat {
  // Creates an indentation string at a given level, where each indent has a
  // width equal to the supplied number of spaces
  def makeIndent (indentSize : Int) (indentNo : Int) : String = {
    " " * (indentNo * indentSize)
  }

  // Given a string and any other object, converts the other object to a
  // string and joins them with a newline
  def joinToStr(curStr : String, anyObj : Any) : String = {
    curStr + "\n" + anyObj.toString()
  }

  // Each phase should have a unique shape, so we provide a static list of
  // shapes here. The first phase will select the first shape, and so on.
  // If we have too many phases, then we will run out of shapes. We can fix
  // these methods if that use case pops up.
  def shapeStrs : Vector[String]

  def availableShapes : Vector[NodeShape] =
    shapeStrs.map ((shapeStr) => new NodeShape(shapeStr))

  def graph : Graph

  // Maybe we are over-specifying how to do format the output, but let's just
  // roll like this for now..
  // Should just use the instance field of the graph to convert to a String
  def formatGraph () : String = {
    val r = 1 until (graph.clusters.length + 1)
    val clusters : IndexedSeq[String] = r.zip(graph.clusters).map(formatCluster)
    val edges : IndexedSeq[String] = graph.edges.map (formatEdge).toIndexedSeq

    joinClustersAndEdges (clusters) (edges)
  }


//  def formatGraph (before : Int, after : Int) : String = {
//    val r = 1 until (graph.clusters.length + 1)
//    val collapsedGraph
//    val clusters : IndexedSeq[String] = r.zip(graph.clusters).map(formatCluster (before, after) (_))
//    val edges : IndexedSeq[String] = graph.edges.map
//
//    joinClustersAndEdges (clusters) (edges)
//  }
//
//  def formatGraph (numVis : Int) : String = {
//    formatGraph (numVis, numVis)
//  }

  // Converts a given phase to its string representation for the output format.
  def formatCluster (indexedCluster : (Int, Cluster)) : String
  def formatShape (shape : NodeShape) : String
  // By default, we ignore the node number parameter passed in
  def formatNumNode (shape : NodeShape) (nodes : List[Node], num : Int, node : Node) : String = {
    formatNode (shape) (node)
  }
  // We create a node for each option. We start with a list of options and
  // return the formatted strings for each of those options.
  def formatNode (shape : NodeShape) (node : Node) : String
  def formatEdge (edge : Edge) : String
  // Given the output string for the clusters and the output string for the
  // edges, we join them along with any other necessary formatting to form
  // the final output string.
  def joinClustersAndEdges (clusters : IndexedSeq[String]) (edges : IndexedSeq[String]) : String
}
