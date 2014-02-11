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
  def yaml() : String = yamlStr

  abstract class YamlStruct
  case class YList(l : Buffer[YamlStruct]) extends YamlStruct
  case class YMap(m : Map[String, YamlStruct]) extends YamlStruct
  case class YVal(v : String) extends YamlStruct

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
            case Some(YList(phases)) => Success(phases.toString())
            case Some(YMap(singlePhase)) => Success("handle single phase")
            case _ => Failure(new MalformedYaml("no phases exist"))
          }
      case _ => Failure(new MalformedYaml("Outer level is a list, not a mapping."))
    }
  }

//  def yaml2Graph() : String = {
//    yamlStructure() match {
//      case None =>
//        ""
//      case Some(Nil) =>
//        ""
//      case Some(List(x : Map[String,Any], _*)) =>
//        x.get("pipeline").productElement(0).toString()
////        x.get("pipeline").foldLeft("")(joinToStr)
//    }
//  }
}

object VizTesting {
  def main(args : Array[String]) {
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

    //(new Viz(yamlStr)).printYamlStruct()
    println((new Viz(yamlStr)).yaml2Graph())
  }
}
