package edu.cmu.lti.oaqa.bagpipes.pipeviz

/*
 * A visualizer tool for creating a data-structure representing pipelines,
 * and then additional tools for outputting that data-structure in various
 * formats (png, Graphviz, LaTeX, etc.).
 */


import org.yaml.snakeyaml.Yaml
import java.util.ArrayList
import java.util.HashMap
import scala.collection.mutable.Buffer
import scala.collection.JavaConverters._
import scala.util.{Try, Success, Failure}
import scala.util.control.ControlThrowable

class MalformedYaml(msg : String) extends Exception(msg)

class Viz(yamlStr : String) {
  abstract class YamlStruct
  case class YList(l : Buffer[YamlStruct]) extends YamlStruct
  case class YMap(m : Map[String, YamlStruct]) extends YamlStruct
  case class YVal(v : String) extends YamlStruct

  // A simple cross product that creates the cross product of two lists.
  // So, this is at most a 2-dimensional cross product.
  def crossProd[A, B] (list1 : List[A]) (list2 : List[B]) : List[(A, B)] = {
    list1.foldLeft (List[(A, B)]()) ((folded: List[(A, B)], x) =>
        folded ++ (list2.map ((y) => (x,y))))
  }

  // Helper function for the more complete cross product function
  def prependToAll[A] (lists : List[List[A]]) (elem : A) : List[List[A]] = {
    lists.map ((aList : List[A]) => elem :: aList)
  }

  // Creates a cross product between every inner list in the argument list
  def crossProd[A] (input : List[List[A]]) : List[List[A]] = {
    input.foldRight (List(Nil) : List[List[A]]) ((aList : List[A], partProd : List[List[A]]) =>
      aList.foldRight (Nil : List[List[A]]) ((x : A, folded : List[List[A]]) => (prependToAll (partProd) (x)) ++ folded)
      )
  }

  // These are the important representation values that we pass along to output
  // formatters.
  val graph : Graph = yaml2Graph().get
  val tree : TreeGraph = renameNodes ("", genTreeBranches (graph.clusters))

  // Accessor to get the yaml string
  def yaml() : String = yamlStr

  // Used to unwrap YAML values. Evaluates to None if we attempt to do so on a
  // list or a map.
  def getYamlVal (yv : YamlStruct) : Option[String] = {
    yv match {
      case YList (l) => None
      case YMap (m) => None
      case YVal (v) => Some (v)
    }
  }

  // Converts the YAML string to a algebraic datatype structure
  def yamlStructure() : YamlStruct =
    yamlStructureHelper(new Yaml().load(yamlStr))

  def yamlStructureHelper(yamlObj : Any) : YamlStruct = {
    yamlObj match {
      case list : ArrayList[_] =>
        // it is a list, so map over all elements after we convert it to Scala
        // form
        YList(list.asScala.map((elem) => elem match {
            case obj : ArrayList[_] => yamlStructureHelper(obj)
            case obj : HashMap[_,_] => yamlStructureHelper(obj)
            case obj : String => YVal(obj)
        }))
      case hashmap : HashMap[String,_] =>
        // it is a map, so we look at each (key, value) after we convert it
        // to Scala form
        val mp : Map[String,_] = Map() ++ (hashmap.asScala)
        YMap(mp.map {
          case (key, value) => (key, yamlStructureHelper(value))
        })
      case otherObj : Any =>
        YVal(otherObj.toString())
    }
  }

  // Converts the algebraic YAML structure to a graph structure
  def yaml2Graph() : Try[Graph] = {
    // Get the phases in the pipeline
    yamlStructure() match {
      case YMap(rootMap) =>
        rootMap.get("pipeline") match {
          // TODO this might not handle the simplified case where we have
          // only 1 phase, so it's not in a list
          case Some(YList(origPhases)) =>
            // Prepend the fake start phase to this list of phases
            val phases : Buffer[YamlStruct] = (YMap(Map(
                  "phase"   -> YVal("Start"),
                  "options" -> YList(Buffer(
                      YMap(Map(
                          "params" -> YMap(Map("start" -> YVal("")))
                      ))
                  ))
                )) +=: origPhases)

            val r = 1 until (phases.length + 1)
            // We convert each phase into a subgraph cluster for the overall graph
            // as well as the list of node names for that cluster.
            val phaseClusters : List[Cluster] =
              r.zip(phases).map(phase2Graph).toList
            val edges : List[Edge] = drawEdges (phaseClusters)
            Success(new Graph(phaseClusters, edges))

          case Some(YMap(singlePhase)) => Failure(new MalformedYaml("we don't yet handle single phases"))
          case _ => Failure(new MalformedYaml("no phases exist"))
        }
      case _ => Failure(new MalformedYaml("Outer level is a list, not a mapping."))
    }
  }

  // Creates all edges between clusters
  def drawEdges (clusters : List[Cluster]) : List[Edge] = {
    clusters match {
      case cluster1 :: cluster2 :: tailClusters =>
        // Draw an edge from every node in the current cluster to every node
        // in the next cluster.
        val headConnections : List[Edge] = connectClusters (cluster1) (cluster2)
        val tailConnections : List[Edge] = drawEdges (cluster2 :: tailClusters)
        // Join all the edges into the same list
        headConnections ++ tailConnections
      case _ => Nil
    }
  }

  // Create an edge between every node in cluster1 to every node in cluster2
  def connectClusters (cluster1 : Cluster) (cluster2 : Cluster) : List[Edge] = {
    val c1Nodes = cluster1.clusterNodes
    val c2Nodes = cluster2.clusterNodes
    (crossProd (c1Nodes) (c2Nodes)).map ({
      case (fromNode, toNode) => new Edge(fromNode, toNode)
    })
  }


  // Converts a given phase to a cluster of nodes within a graph
  private def phase2Graph (x : (Int, YamlStruct)) : Cluster = {
    val clusterNo : Int = x._1
    // We assume the correct YAML structure so we crash if it is input incorrectly
    val YMap(phase) : YamlStruct = x._2

    // We just assign to this case so it will crash if the YAML was not
    // formatted correctly.
    val Some(YVal(phaseName)) : Option[YamlStruct] = phase.get("phase")
    val clusterName = phaseName
    // Try to get options and cross-opts. If we get cross-opts,
    // we just create the cross-product set and add those like they
    // were normal options
    val clusterNodes : List[Node] =
      (phase.get("options"), phase.get("cross-opts")) match {
        // Combine the options with the cross options
        case (Some(YList(options)), Some(YMap(crossOpts))) =>
          val crossedOpts = crossOptsFormatted(crossOpts)
          handleOptions (clusterNo) (options ++ crossedOpts)

        case (Some(YList(options)), _) => handleOptions (clusterNo) (options)

        case (_, Some(YMap(crossOpts))) =>
          handleOptions (clusterNo) (crossOptsFormatted(crossOpts))

        // TODO handle the issue of no options here
        case (_, _) =>  Nil
    }

    (new Cluster(clusterName, clusterNodes))
  }

  // Processes the cross options and formats them as if they were normal
  // options. This allows us to proceed normally after we've formatted
  // the cross options.
  def crossOptsFormatted (crossOpts : Map[String, YamlStruct]) : Buffer[YamlStruct] = {
    // We have a map from parameter names to the list of available values for
    // that parameter. We want to change this so that each element in the list
    // of parameter values is paired up with its parameter name. Then we can
    // throw all of these together in a big nested list and create a
    // cross-product of them.
    val preppedOpts =
      (crossOpts.foldRight (Nil : List[List[(String, YamlStruct)]])
          ({
            case ((pName, YList(pVals)), folded) =>
              (prepareCrossOpt (pName) (pVals)) :: folded
           }))
    val crossedOpts = crossProd (preppedOpts)

    // For each outer list, process it into an item in the YAML options buffer
    val newOpts : List[YamlStruct] =
      crossedOpts.map ((crossElem : List[(String, YamlStruct)]) =>
        // Wrap the parameters in a YMap under the "params" key
        wrapParams (
            // Put the list of items in the cross product in a Map
            YMap(crossElem.foldRight (Map() : Map[String, YamlStruct]) ((kv, folded) =>
              folded + kv))))

    // TODO we should probably just be working in Buffers from the start
    newOpts.toBuffer
  }

  // Given a YamlStruct of parameters, we just wrap it in a YMap under
  // the "params" key
  def wrapParams (params : YamlStruct) : YamlStruct = {
    YMap(Map("params" -> params))
  }

  // We need to match up the opt name with its possible values so that when we
  // take the cross product, we know what parameter each value cooresponds to
  def prepareCrossOpt (optName : String) (optVals : Buffer[YamlStruct]) : List[(String, YamlStruct)] = {
    optVals.map((v : YamlStruct) => (optName, v)).toList
  }

  def handleOptions (clusterNo : Int) (options : Buffer[YamlStruct]) : List[Node] = {
    val clusterNodes : List[Node] = option2Graph (options) (clusterNo)
    clusterNodes
  }

  // We create a node for each option. We start with a list of options and
  // return the nodes for each of those options.
  private def option2Graph (options : Buffer[YamlStruct]) (clusterNo : Int) : List[Node] = {
    def optionMapper (optElem : YamlStruct) : String = {
      val YMap(optMap) : YamlStruct = optElem

      optMap.get("params") match {
        case Some(YMap(params)) =>
          // We create the node name and node label for the given option
          // parameters. The label is every (parameter, value) pair separate
          // by newlines.
          // label=<PARAM : VALUE>,
          //       <PARAM : VALUE>,
          //       ...
          val tmpLabel = params.foldLeft ("") ({
            case (folded : String, (k : String, v : YamlStruct)) =>
              val YVal(paramV) = v

              paramV match {
                // If the value is an empty string, then we do not display it.
                // We also use this when we insert the start phase cluster
                // that actually has no options.
                // TODO Note that we might want to try something a little less
                // hacky here, cause this is using the model in an inconsistent
                // way to get something done easily in the current way the code
                // works.
                case "" => folded + "\\n" + k
                case x => folded + "\\n" + k + ": " + x
              }
          })
          // We remove the leading newline for the label
          tmpLabel.slice(2, tmpLabel.length)
      }
    }

    // For option, we create a node. This part here only gets the
    // labels for the nodes.
    val nodeLabels : List[String] = options.map(optionMapper).toList
    // Add the node names and create the nodes
    addNodeNames (clusterNo) (nodeLabels) (0)
  }

  // Given the node parameter (label, specifically), add the node name to the
  // node as well. When this is done we can instantiate an object of the node
  // class.
  def addNodeNames (clusterNo : Int) (nodeLabels : List[String]) (nodeNo : Int)
    : List[Node] = {
    nodeLabels match {
      case Nil => Nil
      case (head) :: tail =>
        val nodeName : String = "p" + clusterNo.toString + "_" + nodeNo.toString
        val tailNodes : List[Node] = addNodeNames (clusterNo) (tail) (nodeNo + 1)

        (new Node(nodeName, head)) :: tailNodes
    }
  }

  def genTreeBranches (clusters : List[Cluster]) : TreeGraph = {
    clusters match {
      case c :: cs =>
        val treeBelow : TreeGraph = genTreeBranches (cs)
        TreeGraphBranches(c.clusterName, c.clusterNodes.map (x => (x, treeBelow)))
      case Nil => TreeGraphLeaf()
    }
  }

  def renameNodes (namePrefix : String, ct : TreeGraph) : TreeGraph = {
    ct match {
      case TreeGraphLeaf() => TreeGraphLeaf()
      case TreeGraphBranches(cName, theBranches) =>
        TreeGraphBranches(cName, theBranches.map {
          case (node, branch) =>
            val newNode = new Node(namePrefix + node.nodeName, node.nodeLabel)
            val newBranch = renameNodes (newNode.nodeName + "__", branch)
            (newNode, newBranch)
        })
    }
  }
}


/*************************************************
 * Run a test to see if the code works
 ************************************************/
object VizTesting {
  def main(args : Array[String]) : Unit = {
    val yamlStr = ("pipeline:\n"
                    + "  - phase: phase1\n"
                    + "    options:\n"
                    + "      - inherit: dummies.dummyReader\n"
                    + "        params:\n"
                    + "          foo: '0.2229648733341804'\n"
                    + "      - inherit: dummies.dummyReader\n"
                    + "        params:\n"
                    + "          foo: '0.4344104743445896'\n"
                    + "      - inherit: dummies.dummyReader\n"
                    + "        params:\n"
                    + "          foo: '0.48870936286436595'\n"
                    + "  - phase: phase2\n"
                    + "    options:\n"
                    + "      - inherit: dummies.dummyReader\n"
                    + "        params:\n"
                    + "          bar: 'dog'\n"
                    + "      - inherit: dummies.dummyReader\n"
                    + "        params:\n"
                    + "          bar: 'cat'\n"
                    + "  - phase: phase3\n"
                    + "    options:\n"
                    + "      - inherit: dummies.dummyReader\n"
                    + "        params:\n"
                    + "          spam: '1'\n"
                    + "      - inherit: dummies.dummyReader\n"
                    + "        params:\n"
                    + "          spam: '2'\n"
                    + "      - inherit: dummies.dummyReader\n"
                    + "        params:\n"
                    + "          spam: '3'\n"
                    + "      - inherit: dummies.dummyReader\n"
                    + "        params:\n"
                    + "          spam: '4'\n"
                    + "  - phase: phase4\n"
                    + "    cross-opts:\n"
                    + "      parameter-a: [value100, value200]\n"
                    + "      parameter-b: [value300, value400]\n")

    val theViz = new Viz(yamlStr)
    val graphvizFormatter = new GraphvizFormatter(theViz)
    val tikzFormatter = new TikzFormatter(theViz)
    val d3Formatter = new D3Formatter(theViz)
    println(d3Formatter.formatTree())
    // println(graphvizFormatter.formatGraph())
    // println(graphvizFormatter.formatGraph(1,1))
    // println(tikzFormatter.formatGraph())
    // println(tikzFormatter.formatGraph(2, 2))
  }
}
