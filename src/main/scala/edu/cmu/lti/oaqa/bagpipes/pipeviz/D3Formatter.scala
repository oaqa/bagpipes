package edu.cmu.lti.oaqa.bagpipes.pipeviz

import net.liftweb.json._

class D3Formatter (vizArg : Viz) extends VizOutputFormat {
  val viz = vizArg

  val shapeStrs = Vector("std")


  // Note that none of these functions do anything.
  def formatCluster (indexedCluster : (Int, Cluster)) : String = {
    ""
  }
  def formatShape (shape : NodeShape) : String = {
    ""
  }
  def formatEdge (edge : Edge) : String = {
    ""
  }
  def formatNode (shape : NodeShape) (node : Node) : String = {
    ""
  }
  def joinClustersAndEdges (clusters : IndexedSeq[String]) (edges : IndexedSeq[String]) = {
    ""
  }

//  case class TreeGraphLeaf() extends TreeGraph
//  case class TreeGraphBranches(cName : String, branches : List[(Node, TreeGraph)]) extends TreeGraph
  def formatTree (tree : TreeGraph) : String = {
    pretty(render(
        jsonFormatTree (tree) match {
          case JArray(jsonListItems) => jsonListItems.head
          case somethingElse => somethingElse
        }))
    // pretty(render(jsonFormatTree (tree)))
  }

  private def jsonFormatTree (tree : TreeGraph) : JValue = {
    tree match {
      case TreeGraphLeaf() => JField("size", JInt(1000))
      case TreeGraphBranches(cName, branches) =>
        JArray(branches.map {
          case (node : Node, subtree : TreeGraph) =>
            val jName = JField("name", JString(node.nodeLabel))
            val subJson = jsonFormatTree(subtree)
            // Another JSON field that is either a nested list of child nodes
            // or a size value, depending on whether we are at a leaf.
            val childrenOrSize : JValue = subJson match {
              case JField("size", size) => subJson
              case _ => JField("children", subJson)
            }
            subJson match {
              case JField("size", size) => JObject(List(jName, JField("size", size)))
              case _ => JObject(List(jName, JField("children", subJson)))
            }
        })
    }
  }
}