package edu.cmu.lti.oaqa.bagpipes.pipeviz

/**
 * The output format for Tikz.
 * As a reminder, to get the ball rolling and output the whole graph as a
 * formatted Tikz string, use the method `formatGraph`.
 */

class TikzFormatter (vizArg : Viz) extends VizOutputFormat {
  val viz : Viz = vizArg

  val shapeStrs = Vector("box", "ellipse", "rectangle", "diamond",
      "parallelogram", "house", "hexagon", "trapezium", "egg",
      "circle", "oval")

  // Converts a given phase to its string representation for the output format.
  def formatCluster (x : (Int, Cluster)) : String = {
    val clusterNo : Int = x._1
    val clusterShape : NodeShape = availableShape(clusterNo)
    val cluster : Cluster = x._2

    // All clusters after the first one must be positioned to the right of the
    // previous cluster.
    val clusterPos = clusterNo match {
      case 1 => "[scoped]"
      case n => "[scoped, right of=scope" + (clusterNo-1).toString() + "]"
    }

    val clusterNode = "\\node (scope" + clusterNo.toString() + ") " + clusterPos + " {";
    val r = 0 until (cluster.clusterNodes.length)
    val numberedNodes = r.zip(cluster.clusterNodes)
    val nodes : IndexedSeq[String] =
      numberedNodes.map {case (i, node) => formatNumNode (clusterShape) (cluster.clusterNodes, i, node)}
    // We insert the phase label as a node
    val nodesWithPhases : IndexedSeq[String] = insertPhaseNodes (clusterNo) (nodes)

    ((makeIndent (2) (1)) + clusterNode + "\n"
        + (makeIndent (2) (2)) + "\\begin{tikzpicture}" + "\n"
        + (makeIndent (2) (3)) + nodesWithPhases.mkString("\n" + (makeIndent (2) (3))) + "\n"
        + (makeIndent (2) (2)) + "\\end{tikzpicture}" + "\n"
        + (makeIndent (2) (1)) + "};")
  }

  private def insertPhaseNodes (clusterNo : Int) (nodes : IndexedSeq[String]) : IndexedSeq[String] = {
    // Prepare the string for the phase node
    val cNoStr = clusterNo.toString()
    val phaseStr = clusterNo match {
      case 1 => "\\underline{\\textbf{" + "Start" + "}}"
      case n => "\\underline{\\textbf{" + "Phase " + (clusterNo - 1).toString() + "}}"
    }
    val phaseNodeStr = "\\node (p" + cNoStr + "_title) [above=1cm of p" + cNoStr + "_0] {" + phaseStr + "};"

    // Insert the phase node into the sequence of nodes
    (nodes.take(1) :+ phaseNodeStr) ++ nodes.drop(1)
  }

  def formatShape (shape : NodeShape) : String = {
    "std"
  }

  // Have a default def defined, so we must override it.
  override def formatNumNode (shape : NodeShape) (nodes : List[Node], num : Int, node : Node) : String = {
    val locText = num match {
      // We do not position the first node relative to anything, so we don't
      // need any extra positioning text.
      case 0 => ""
      // We need relative positioning
      case n => {
        val clusterNo = node.nodeName.slice(1, node.nodeName.indexOf('_'))
        ", below of=" + nodes(n-1).nodeName
      }
    }

    ("\\node (" + node.nodeName + ") ["
        + (formatShape (shape)) + locText + "] {"
        + node.nodeLabel.replaceAll("""\\n""", """ \\\\""" + "\n" + (makeIndent (2) (4))) + "};")
  }

  // We create a node for each option. We start with a list of options and
  // return the formatted strings for each of those options.
  // You should not use this unless you want Tikz nodes to overlap and have
  // other formatting issues.
  def formatNode (shape : NodeShape) (node : Node) : String = {
    formatNumNode (shape) (List(), 0, node)
  }

  def formatEdge (edge : Edge) : String = {
    ("\\draw[->] (" + edge.fromNode.nodeName + ") -- ("
        + edge.toNode.nodeName + ");")
  }

  // This super-long line is the string of LaTeX content we need to include
  // before drawing the nodes.
  var latexSetupText = "\\documentclass[a4paper, landscape]{minimal}\n\n\\usepackage{tikz}\n\\usepackage[a4paper, landscape]{geometry}\n\\usetikzlibrary{arrows, shapes, trees, positioning, chains}\n\n\\usepackage[utf8]{inputenc}\n\n\n\\begin{document}\n\n% For every picture that defines or uses external nodes, you\'ll have to apply\n% the \"remember picture\" style. To avoid some typing, we\'ll apply the style to\n% all pictures.\n\\tikzstyle{every picture}+=[remember picture]\n\\begin{tikzpicture}[\n    >=triangle 60,\n    auto,\n    node distance=4cm,  % global setup of box spacing\n  ]\n\n  \\tikzset{\n    base/.style={draw, on grid, align=center, minimum height=4ex},\n    % For the way we group phases into a scope. We put a box around them.\n    scoped/.style={base, rectangle, line width=0.2pt},\n    std/.style={base, rectangle}\n  }"
  var latexClosingText = "\\end{tikzpicture}\n\\end{document}"

  // Given the output string for the clusters and the output string for the
  // edges, we join them along with any other necessary formatting to form
  // the final output string.
  def joinClustersAndEdges (clusters : IndexedSeq[String]) (edges : IndexedSeq[String]) : String = {
    (latexSetupText + "\n\n"
        + clusters.mkString("\n") + "\n\n"
        + (makeIndent (2) (1)) + edges.mkString("\n" + (makeIndent (2) (1))) + "\n"
        + latexClosingText + "\n")
  }

  // TODO implement this feature
  def formatTree (tree : TreeGraph) : String = {
    ""
  }
}