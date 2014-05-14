package edu.cmu.lti.oaqa.bagpipes.pipeviz

abstract class TreeGraph {
  // TODO actually write this part up
  def toGraph () : Graph = {
    new Graph (List() : List[Cluster], Set() : Set[Edge])
  }
}
case class TreeGraphLeaf() extends TreeGraph
case class TreeGraphBranches(cName : String, branches : List[(Node, TreeGraph)]) extends TreeGraph
