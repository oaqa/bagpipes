package edu.cmu.lti.oaqa.bagpipes.cmd

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
//import uk.co.turingatemyhamster.graphvizs.dsl._

class MalformedYaml(msg : String) extends Exception(msg)

class Viz(yamlStr : String) {
  abstract class YamlStruct
  case class YList(l : Buffer[YamlStruct]) extends YamlStruct
  case class YMap(m : Map[String, YamlStruct]) extends YamlStruct
  case class YVal(v : String) extends YamlStruct

  // Each phase should have a unique shape, so we provide a static list of
  // shapes here. The first phase will select the first shape, and so on.
  // If we have too many phases, then we will run out of shapes. We can fix
  // these methods if that use case pops up.
  def graphvizShapes() : List[String] = List("box", "ellipse", "oval",
      "diamond", "parallelogram", "house", "hexagon", "rectangle",
      "trapezium", "egg", "circle")

  // This gets a specific shape instead of the whole list.
  def graphvizShapes(i : Int) : String = {
    val shapes = Vector("box", "ellipse", "oval", "diamond", "parallelogram",
        "house", "hexagon", "rectangle", "trapezium", "egg", "circle")
    shapes(i)
  }

  def yaml() : String = yamlStr

  def getYamlVal (yv : YamlStruct) : Option[String] = {
    yv match {
      case YList (l) => None
      case YMap (m) => None
      case YVal (v) => Some (v)
    }
  }

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

  def printYamlStruct() : Unit = println(yamlStructure())

  def joinToStr(curStr : String, anyObj : Any) : String = {
    println(anyObj.toString())
    curStr + "\n" + anyObj.toString()
  }


  def yaml2Graph() : Try[String] = {
    // Get the phases in the pipeline
    yamlStructure() match {
      case YMap(rootMap) =>
          rootMap.get("pipeline") match {
            // TODO this might not handle the simplified case where we have
            // only 1 phase, so it's not in a list
            case Some(YList(phases)) =>
              val r = 1 until (phases.length + 1)
              // We convert each phase into a subgraph cluster for the overall graph
              val phaseParts : IndexedSeq[String] = r.zip(phases).map(phase2Graph)
              Success(phaseParts.mkString("\n"))
            case Some(YMap(singlePhase)) => Success("handle single phase")
            case _ => Failure(new MalformedYaml("no phases exist"))
          }
      case _ => Failure(new MalformedYaml("Outer level is a list, not a mapping."))
    }
  }


  // Converts a given phrase to its string representation for Graphviz.
  private def phase2Graph (x : (Int, YamlStruct)) : String = {
    val clusterNo : Int = x._1
    val clusterShape : String = graphvizShapes(clusterNo)
    val YMap(phase) : YamlStruct = x._2

    // We just assign to this case so it will crash if the YAML was not
    // formatted correctly.
    val Some(YVal(phaseName)) : Option[YamlStruct] = phase.get("phase")
    val clusterLabel = "label=\"" + phaseName
    val Some(YList(options)) : Option[YamlStruct] = phase.get("options")
    val nodeParams : List[String] = option2Graph (options) (clusterShape)
    // We create a subgraph section. This includes the section header, a
    // subgraph cluster label, and the list of nodes within that subgraph
    ("subgraph cluster_" + clusterNo.toString() + " {\n"
        + clusterLabel + "\n"
        // TODO we need to prepend the node names here and also
        // return a list of the node names
        + nodeParams.mkString("\n")
        + "\n}")
  }

  // We create a node for each option. We start with a list of options and
  // return the formatted strings for each of those options.
  def option2Graph (options : Buffer[YamlStruct]) (shape : String) : List[String] = {
    // The inner function that folds over the buffer of YamlStructs.
    // We do this so we can use a closure to make every shape equal the shape
    // input to this function.
    // Each run of this converts an option into the graphviz parameter string
    // representing that option's parameters.
    def optionFolder (optElem : YamlStruct, folded : List[String]) : List[String] = {
      val YMap(optMap) : YamlStruct = optElem

      optMap.get("params") match {
        case Some(YMap(params)) =>
          // We create the string that applies options to a Graphviz node.
          // Node options are in the following format:
          //   [label="<PARAM : VALUE>", shape=<PHASE SHAPE>]
          val aLabel = params.foldLeft ("") ({
            case (folded : String, (k : String, v : YamlStruct)) =>
              val YVal(paramV) = v
              folded + "\\n" + k + ": " + paramV
          })
          // We remove the leading newline add add in the shape
          val nodeParams : String = (
              "[label=\"" + aLabel.slice(2,aLabel.length) + "\", shape=" + shape + "]")

          nodeParams :: folded

        // Otherwise the YAML is formatted badly, so we skip this option while reducing
        case _ => folded
      }
    }

    // For option, we create a node in Graphviz. This part here only gets the
    // parameters for the node. We will insert the node name after.
    options.foldRight (List[String]()) (optionFolder)
  }
}

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
                   + "          spam: '4'\n")

    (new Viz(yamlStr)).yaml2Graph() match {
      case Success(str) => println(str)
      case _ => ()
    }
  }
}
