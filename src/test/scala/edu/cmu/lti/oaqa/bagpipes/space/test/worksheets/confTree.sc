package edu.cmu.lti.oaqa.cse.space.scala.test.worksheets
import net.liftweb.json.JsonAST._
import net.liftweb.json.Extraction._
import net.liftweb.json.Printer._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import net.liftweb.json.parse
import net.liftweb.json.DefaultFormats
import net.liftweb.json.{ Serialization, ShortTypeHints }
import scala.util.parsing.json.JSONObject
import org.junit.runner.RunWith
import org.scalatest.FeatureSpec
import org.junit._
import org.scalatest.junit.JUnitRunner
import scala.reflect.ClassManifest
import edu.cmu.lti.oaqa.cse.scala.configuration.YAMLParser
import edu.cmu.lti.oaqa.cse.scala.configuration.Parser
import edu.cmu.lti.oaqa.cse.scala.configuration.Implicits._
import edu.cmu.lti.oaqa.cse.scala.space.ConfigurationSpace
import edu.cmu.lti.oaqa.cse.scala.space.ConfigurationSpace._
import edu.cmu.lti.oaqa.cse.scala.configuration._
import edu.cmu.lti.oaqa.cse.CommonTesting._
import scala.io.Source._
import java.io.InputStream
import edu.cmu.lti.oaqa.cse.scala.configuration.Parameters._
object confTree {
  new yamls {
    val parser: Parser = YAMLParser
    // val parsedEx0 = parser.parse(ex0)
    // println(parsedEx0)
    //  val parsedEx1 = parser.parse(ex1)
    //val confSpace = ConfigurationSpace
 
    // confSpace.populateTree(parsedEx1)

    //fromInputStream(getClass.getResourceAsStream("/collection_reader/filesystem-collection-reader.yaml")).mkString
    //val parsedEx3 = parser.parse(ex3)
    //val space3 = ConfigurationSpace(parsedEx3).getSpace
    
    val x =List(("a","b"),("c","d")).toMap.apply("a")
    
    val paramList =  List(("opt1","a" :: "b" :: "c" :: Nil),("opt2",List(1,2)))
    type CrossOptParam = (String, List[_])
    private def crossOptParams(paramPairs: List[CrossOptParam]): List[Map[String, Parameter]] = {
      def genAllPairs(cParam: CrossOptParam): List[(String,Parameter)] = cParam._2.map( v =>(cParam._1, primitive2Parameter(v)))
      paramPairs match {
        case Nil => List(Map())
        case head :: tail =>
          for {
            pair <- genAllPairs(head)
            next <- crossOptParams(tail)
          } yield (Map(pair) ++ next)
      }
    }
  
  
  val y= crossOptParams(paramList)
  println(y)
  }                                               //> List(Map(opt1 -> StringParameter(a), opt2 -> IntegerParameter(1)), Map(opt1
                                                  //|  -> StringParameter(a), opt2 -> IntegerParameter(2)), Map(opt1 -> StringPar
                                                  //| ameter(b), opt2 -> IntegerParameter(1)), Map(opt1 -> StringParameter(b), op
                                                  //| t2 -> IntegerParameter(2)), Map(opt1 -> StringParameter(c), opt2 -> Integer
                                                  //| Parameter(1)), Map(opt1 -> StringParameter(c), opt2 -> IntegerParameter(2))
                                                  //| )
                                                  //| res0: edu.cmu.lti.oaqa.cse.CommonTesting.yamls{val parser: edu.cmu.lti.oaqa
                                                  //| .cse.scala.configuration.Parser; val x: String; val paramList: List[(String
                                                  //| , List[Any])]; type CrossOptParam = (String, List[_]); val y: List[Map[Stri
                                                  //| ng,edu.cmu.lti.oaqa.cse.scala.configuration.Parameters.Parameter]]} = edu.c
                                                  //| mu.lti.oaqa.cse.space.scala.test.worksheets.confTree$$anonfun$main$1$$anon$
                                                  //| 1@100c62c8
}