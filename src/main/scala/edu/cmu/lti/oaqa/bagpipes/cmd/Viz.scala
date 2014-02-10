package edu.cmu.lti.oaqa.bagpipes.cmd

/*
 * A visualizer tool for creating a data-structure representing pipelines,
 * and then additional tools for outputting that data-structure in various
 * formats (png, Graphviz, LaTeX, etc.).
 */


import org.yaml.snakeyaml.Yaml
import java.util.ArrayList
import java.util.HashMap
import scala.collection.JavaConverters._
//import uk.co.turingatemyhamster.graphvizs.dsl._

class Viz(yamlStr : String) {
  def yaml() : String = yamlStr

  def yamlStructure() = {
    new Yaml().load(yamlStr) match {
      case obj: ArrayList[_] =>
        val seqOfMaps = obj.asScala.collect {
          case hashMap: HashMap[_, _] =>
            hashMap.asScala.toMap.asInstanceOf[Map[String, Any]]
        }.toSeq
        if (seqOfMaps.isEmpty) None else Some(seqOfMaps)
      case obj: HashMap[_, _] =>
        Some(Seq(obj.asScala.toMap.asInstanceOf[Map[String, Any]]))
      case obj: Any =>
        None
    }
  }

  def printYamlStruct() : Unit = println(yamlStructure())
  
  def joinToStr(curStr : String, anyObj : Any) : String = {
    println(anyObj.toString())
    curStr + "\n" + anyObj.toString()
  }
  
  def yaml2Graph() : String = {
    yamlStructure() match {
      case None =>
        ""
      case Some(Nil) =>
        ""
      case Some(List(x : Map[String,Any], _*)) =>
        x.get("pipeline").productElement(0).toString()
//        x.get("pipeline").foldLeft("")(joinToStr)
    }
  }
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
