package edu.cmu.lti.oaqa.cse.configuration.test
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor._
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
import scala.util.parsing.json.JSONFormat.defaultFormatter
object yamlTest {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(623); 
 
  val i = 0

  case class Foo(bar: JSONObject)

  case class RoomAnnotator(foo: String, map: Map[String, Any]);System.out.println("""i  : Int = """ + $show(i ));$skip(147); 

  val ex0 =
"""
bar:
  baz: aaa
  cas: blah""";System.out.println("""ex0  : String = """ + $show(ex0 ));$skip(276); 

  val ex1 = """
foo: blah
locations: [Watson - Yorktown, Watson - Hawthrone I,Watson - Hawthorne II]
patterns: ["\\b[0-4]\\d[0-2]\\d\\d\\b","\\b[G1-4][NS]-[A-Z]\\d\\d\\b","\\bJ[12]-[A-Z]\\d\\d\\b"]
#persistence-provider: |
#  inherit: ecd.default-log-persistence-provider""";System.out.println("""ex1  : String = """ + $show(ex1 ));$skip(174); 
  val ex2 = """
configuration:
  name: oaqa-tutorial
  author: oaqa

collection-reader:
  inherit: collection_reader.filesystem-collection-reader
  InputDirectory: data/
""";System.out.println("""ex2  : String = """ + $show(ex2 ));$skip(41); 

  implicit val formats = DefaultFormats;System.out.println("""formats  : net.liftweb.json.DefaultFormats.type = """ + $show(formats ));$skip(24); 
  val yaml = new Yaml();System.out.println("""yaml  : org.yaml.snakeyaml.Yaml = """ + $show(yaml ));$skip(80); 
  val map = JSONObject(yaml.load(ex0).asInstanceOf[java.util.Map[String, Any]]);System.out.println("""map  : <error> = """ + $show(map ));$skip(81); 
  val map2 = JSONObject(yaml.load(ex1).asInstanceOf[java.util.Map[String, Any]]);System.out.println("""map2  : <error> = """ + $show(map2 ));$skip(104); val res$0 = 
  //map2.obj.get("locations").get.asInstanceOf[java.util.List[String]].asScala.toList
  decompose(map2);System.out.println("""res0: net.liftweb.json.JValue = """ + $show(res$0));$skip(42); 
  val parsedEx0 = decompose(map) \\ "obj";System.out.println("""parsedEx0  : net.liftweb.json.JsonAST.JValue = """ + $show(parsedEx0 ));$skip(18); val res$1 = 
  map2.toString();System.out.println("""res1: <error> = """ + $show(res$1));$skip(43); 
  val parsedEx1 = decompose(map2) \\ "obj";System.out.println("""parsedEx1  : net.liftweb.json.JsonAST.JValue = """ + $show(parsedEx1 ));$skip(262); 
  // val map3 = YAMLParser.parse(ex2)
 // val jsonObj = new JSONObject(Map("foo"->"bar"))
//  val decomp = parse(compact(render(decompose(parsedEx0))))
 // decomp.extract[Foo]
  
 // val formatter = defaultFormatter
  
  //decomp.extract[Map[_,_]]
  val x = "1";System.out.println("""x  : String = """ + $show(x ));$skip(26); 
  def printNum(n:Int) = n;System.out.println("""printNum: (n: Int)Int""");$skip(47); 
  implicit def stringToInt(n:String) = n.toInt;System.out.println("""stringToInt: (n: String)Int""");$skip(14); val res$2 = 
  printNum(x);System.out.println("""res2: Int = """ + $show(res$2))}
  //parsedEx1.extract[RoomAnnotator]

}
