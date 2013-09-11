package edu.cmu.lti.oaqa.cse.configuration.scala.test.worksheets
import net.liftweb.json.JsonAST._
import net.liftweb.json.Extraction._
import net.liftweb.json.Printer._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import net.liftweb.json.parse
import edu.cmu.lti.oaqa.cse.scala.configuration.Implicits._
import net.liftweb.json.DefaultFormats
import net.liftweb.json.{ Serialization, ShortTypeHints }
import net.liftweb.json.JsonAST.JInt._
import scala.util.parsing.json.JSONObject

import scala.io.Source._
import java.io.InputStream
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST._
import net.liftweb.json.Extraction._
import net.liftweb.json.Serialization
import net.liftweb.json.ShortTypeHints
import edu.cmu.lti.oaqa.cse.CommonTesting._
import edu.cmu.lti.oaqa.cse.scala.configuration.Parser
import edu.cmu.lti.oaqa.cse.scala.configuration.YAMLParser._

object json {

  4 equals JInt(4)                                //> res0: Boolean = false
 // val x = "check"
  //JString("check") == x
  trait Listable
  implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[CollectionReaderDescriptor], classOf[B],classOf[C])))
                                                  //> formats  : net.liftweb.json.Formats = net.liftweb.json.Serialization$$anon$
                                                  //| 1@4ce32802
  sealed trait ExecutableConf //extends ExecutableDescriptor with ConfExpr
  sealed trait Testable //extends ExecutableDescriptor with ConfExpr

  sealed trait ExecutableComponent extends ExecutableConf
  case class ConfigurationDescriptor(configuration: Configuration, `collection-reader`: ExecutableConf, pipeline: List[PhaseDescriptor])
  case class Configuration(name: String = "default-config", author: String = "default-author")
  case class CollectionReaderDescriptor(name: String) extends /* ParameterizedDescriptor(`class`, params) with*/ ExecutableConf
  case class D(name: String) extends  Testable
  case class PhaseDescriptor(name: String, options: List[ExecutableComponent]) extends ExecutableConf
  case class ComponentDescriptor(`class`: String, params: Map[String, B] = Map()) extends /*ParameterizedDescriptor(`class`, params) with*/ ExecutableComponent
  case class CrossComponentDescriptor(`class`: String, params: Map[String, B] = Map(), `cross-opts`: Map[String, List[B]] = Map()) extends /* ParameterizedDescriptor(`class`, params) with*/ ExecutableComponent
  case class ScoreDescriptor(cost: Double, benefit: Double)

  case class C(listy: List[Listable]) extends Listable
 // case class A(what: String, who: String) extends Listable
  case class B(name: String) extends Listable
  val decomp = decompose(
    JSONObject(Map("listy" -> List( B("name"))))) \ "obj"
                                                  //> decomp  : net.liftweb.json.JsonAST.JValue = JObject(List(JField(listy,JArra
                                                  //| y(List(JObject(List(JField(jsonClass,JString(json$$anonfun$main$1$B$3)), JF
                                                  //| ield($outer,JObject(List())), JField(name,JString(name)))))))))
  new yamls {
    //  flattenConfMap(parse(ex2))
  }                                               //> res1: edu.cmu.lti.oaqa.cse.CommonTesting.yamls = edu.cmu.lti.oaqa.cse.confi
                                                  //| guration.scala.test.worksheets.json$$anonfun$main$1$$anon$1@70453807
  val y = decomp.extract[Listable]                //> net.liftweb.json.MappingException: No constructor for type interface edu.cm
                                                  //| u.lti.oaqa.cse.configuration.scala.test.worksheets.json$$anonfun$main$1$Lis
                                                  //| table$1, JObject(List(JField(listy,JArray(List(JObject(List(JField(jsonClas
                                                  //| s,JString(json$$anonfun$main$1$B$3)), JField($outer,JObject(List())), JFiel
                                                  //| d(name,JString(name)))))))))
                                                  //| 	at net.liftweb.json.Meta$.fail(Meta.scala:191)
                                                  //| 	at net.liftweb.json.Extraction$$anonfun$findBestConstructor$1$1.apply(Ex
                                                  //| traction.scala:216)
                                                  //| 	at net.liftweb.json.Extraction$$anonfun$findBestConstructor$1$1.apply(Ex
                                                  //| traction.scala:216)
                                                  //| 	at scala.Option.getOrElse(Option.scala:120)
                                                  //| 	at net.liftweb.json.Extraction$.findBestConstructor$1(Extraction.scala:2
                                                  //| 16)
                                                  //| 	at net.liftweb.json.Extraction$.instantiate$1(Extraction.scala:251)
                                                  //| 	at net.liftweb.json.Extraction$.newInstance$1(Extraction.scala:286)
                                                  //| 	at net.liftweb.json.Extraction$.net$liftweb$json$Extraction$$build$1(Ext
                                                  //| rac
                                                  //| Output exceeds cutoff limit.

}