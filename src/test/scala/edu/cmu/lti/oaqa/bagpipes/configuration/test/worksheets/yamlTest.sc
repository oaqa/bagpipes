package edu.cmu.lti.oaqa.cse.configuration.test
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor._
import edu.cmu.lti.oaqa.cse.configuration.test.DeepJavaConverter._
//implicit val formats = net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST._
import net.liftweb.json.Extraction._
import net.liftweb.json.Printer._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import net.liftweb.json.parse
import net.liftweb.json.DefaultFormats
import net.liftweb.json.{ Serialization, ShortTypeHints }
import scala.util.parsing.json.JSONObject
import edu.cmu.lti.oaqa.cse.scala.configuration.YAMLParser
import scala.util.parsing.json.JSONFormat.defaultFormatter
object yamlTest {
 
  val i = 0                                       //> i  : Int = 0

  case class Foo(bar: JSONObject)

  case class RoomAnnotator(foo: String, map: Map[String, Any])

  val ex0 =
"""
bar:
  baz: aaa
  cas: blah"""                                    //> ex0  : String = "
                                                  //| bar:
                                                  //|   baz: aaa
                                                  //|   cas: blah"

  val ex1 = """
foo: blah
locations: [Watson - Yorktown, Watson - Hawthrone I,Watson - Hawthorne II]
patterns: ["\\b[0-4]\\d[0-2]\\d\\d\\b","\\b[G1-4][NS]-[A-Z]\\d\\d\\b","\\bJ[12]-[A-Z]\\d\\d\\b"]
#persistence-provider: |
#  inherit: ecd.default-log-persistence-provider"""
                                                  //> ex1  : String = "
                                                  //| foo: blah
                                                  //| locations: [Watson - Yorktown, Watson - Hawthrone I,Watson - Hawthorne II]
                                                  //| patterns: ["\\b[0-4]\\d[0-2]\\d\\d\\b","\\b[G1-4][NS]-[A-Z]\\d\\d\\b","\\bJ
                                                  //| [12]-[A-Z]\\d\\d\\b"]
                                                  //| #persistence-provider: |
                                                  //| #  inherit: ecd.default-log-persistence-provider"
  val ex2 = """
configuration:
  name: oaqa-tutorial
  author: oaqa

collection-reader:
  inherit: collection_reader.filesystem-collection-reader
  InputDirectory: data/
"""                                               //> ex2  : String = "
                                                  //| configuration:
                                                  //|   name: oaqa-tutorial
                                                  //|   author: oaqa
                                                  //| 
                                                  //| collection-reader:
                                                  //|   inherit: collection_reader.filesystem-collection-reader
                                                  //|   InputDirectory: data/
                                                  //| "

  implicit val formats = DefaultFormats           //> formats  : net.liftweb.json.DefaultFormats.type = net.liftweb.json.DefaultF
                                                  //| ormats$@7f2ad19e
  val yaml = new Yaml()                           //> yaml  : org.yaml.snakeyaml.Yaml = Yaml:1544025324
  val map = JSONObject(yaml.load(ex0).asInstanceOf[java.util.Map[String, Any]])
                                                  //> map  : scala.util.parsing.json.JSONObject = {"bar" : Map(baz -> aaa, cas ->
                                                  //|  blah)}
  val map2 = JSONObject(yaml.load(ex1).asInstanceOf[java.util.Map[String, Any]])
                                                  //> map2  : scala.util.parsing.json.JSONObject = {"foo" : "blah", "locations" :
                                                  //|  List(Watson - Yorktown, Watson - Hawthrone I, Watson - Hawthorne II), "pat
                                                  //| terns" : List(\b[0-4]\d[0-2]\d\d\b, \b[G1-4][NS]-[A-Z]\d\d\b, \bJ[12]-[A-Z]
                                                  //| \d\d\b)}
  //map2.obj.get("locations").get.asInstanceOf[java.util.List[String]].asScala.toList
  decompose(map2)                                 //> res0: net.liftweb.json.JValue = JObject(List(JField(obj,JObject(List(JField
                                                  //| (foo,JString(blah)), JField(locations,JArray(List(JString(Watson - Yorktown
                                                  //| ), JString(Watson - Hawthrone I), JString(Watson - Hawthorne II)))), JField
                                                  //| (patterns,JArray(List(JString(\b[0-4]\d[0-2]\d\d\b), JString(\b[G1-4][NS]-[
                                                  //| A-Z]\d\d\b), JString(\bJ[12]-[A-Z]\d\d\b)))))))))
  val parsedEx0 = decompose(map) \\ "obj"         //> parsedEx0  : net.liftweb.json.JsonAST.JValue = JObject(List(JField(bar,JObj
                                                  //| ect(List(JField(baz,JString(aaa)), JField(cas,JString(blah)))))))
  map2.toString()                                 //> res1: String = {"foo" : "blah", "locations" : List(Watson - Yorktown, Watso
                                                  //| n - Hawthrone I, Watson - Hawthorne II), "patterns" : List(\b[0-4]\d[0-2]\d
                                                  //| \d\b, \b[G1-4][NS]-[A-Z]\d\d\b, \bJ[12]-[A-Z]\d\d\b)}
  val parsedEx1 = decompose(map2) \\ "obj"        //> parsedEx1  : net.liftweb.json.JsonAST.JValue = JObject(List(JField(foo,JStr
                                                  //| ing(blah)), JField(locations,JArray(List(JString(Watson - Yorktown), JStrin
                                                  //| g(Watson - Hawthrone I), JString(Watson - Hawthorne II)))), JField(patterns
                                                  //| ,JArray(List(JString(\b[0-4]\d[0-2]\d\d\b), JString(\b[G1-4][NS]-[A-Z]\d\d\
                                                  //| b), JString(\bJ[12]-[A-Z]\d\d\b))))))
  // val map3 = YAMLParser.parse(ex2)
 // val jsonObj = new JSONObject(Map("foo"->"bar"))
//  val decomp = parse(compact(render(decompose(parsedEx0))))
 // decomp.extract[Foo]
  
 // val formatter = defaultFormatter
  
  //decomp.extract[Map[_,_]]
  val x = "1"                                     //> x  : String = 1
  def printNum(n:Int) = n                         //> printNum: (n: Int)Int
  implicit def stringToInt(n:String) = n.toInt    //> stringToInt: (n: String)Int
  printNum(x)                                     //> res2: Int = 1
  //parsedEx1.extract[RoomAnnotator]

}