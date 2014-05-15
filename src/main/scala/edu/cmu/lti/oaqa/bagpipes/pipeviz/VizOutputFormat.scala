package edu.cmu.lti.oaqa.bagpipes.pipeviz

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

  // Use this to set the node shape per cluster. Wraps around by modular
  // arithmetic.
  def availableShape (shapeNo : Int) : NodeShape = {
    val len = availableShapes.length
    availableShapes(shapeNo % len)
  }

  def viz : Viz

  ///////////////////////////////////
  // Graph related formatting code //
  ///////////////////////////////////

  // Maybe we are over-specifying how to do format the output, but let's just
  // roll like this for now...
  // This is a private method that allows us to just pass all of the overloaded
  // forms in through here. From the outside, we should not pass a specific
  // graph into the visualizer besides during object instantiation.
  private def formatGraph (graph : Graph) : String = {
    val r = 1 until (graph.clusters.length + 1)
    val clusters : IndexedSeq[String] = r.zip(graph.clusters).map(formatCluster)
    val edges : IndexedSeq[String] = graph.edges.map (formatEdge).toIndexedSeq

    joinClustersAndEdges (clusters) (edges)
  }
  // This is the public facing method to visualize the graph. It just pipes the
  // operation over to the private formatGraph method, which then does the real
  // work.
  // Should just use the instance field of the graph to convert to a String
  def formatGraph () : String = {
    formatGraph(viz.graph)
  }
  // This is the public facing method to visualize a collapsed graph.
  def formatGraph (before : Int, after : Int) : String = {
    formatGraph (viz.graph,
        viz.graph.collapseGraph(before, after))
  }
  // This is what does the actual work for visualizing a collapsed graph.
  // By default, this will only view the collapsed graph.
  // We allow implementations of this trait to define this differently.
  // For example, there might be a hybrid visualization of the collapsed and
  // standard graph.
  private def formatGraph (standard : Graph, collapsed : Graph) : String = {
    formatGraph(collapsed)
  }

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


  //////////////////////////////////
  // Tree related formatting code //
  //////////////////////////////////
  def formatTree (tree : TreeGraph) : String

  def formatTree () : String = {
    formatTree (viz.tree)
  }
}
