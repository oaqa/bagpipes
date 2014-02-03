package edu.cmu.lti.oaqa.bagpipes.configuration

import edu.cmu.lti.oaqa.bagpipes.configuration._
import net.liftweb.json.Extraction._
import scala.io.Source._
import java.io.InputStream
import edu.cmu.lti.oaqa.bagpipes.configuration.Parameters._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors._
import net.liftweb.json.DefaultFormats
import net.liftweb.json.ShortTypeHints
import net.liftweb.json.FullTypeHints
import net.liftweb.json.TypeHints

/**
 * A collection of utility methods used by [[$packagePath.Parser]].
 *
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

object ParserUtils {

  /**
   * Returns a new [[scala.Map]] from combining the maps shared by
   * a common key in `map1` and `map2`.
   *
   * For example,
   * {{{
   * val map1 = Map("foo" -> Map("a" -> "bar", "b" -> "blah"), "baz" -> "derp")
   * val map2 = Map("baz" -> "derp", "foo" -> Map("c" -> "hello"))
   *
   * joinMaps(map1,map2,"foo")
   * > Map("a" -> "bar", "b" -> "blah","c" -> "hello")
   * }}}  ``
   */
  def joinMaps(map1: Map[String, _], map2: Map[String, _], on: String): Map[String, Parameter] = {
    val paramMap1 = map1.get(on)
    val paramMap2 = map2.get(on)
    val mapCombined: Map[String, _] = (paramMap1, paramMap2) match {
      case (None, None) => Map()
      case (m1, None) => m1.get.asInstanceOf[Map[String, _]]
      case (None, m2) => m2.get.asInstanceOf[Map[String, _]]
      case (m1, m2) => (m1.get.asInstanceOf[Map[String, _]] ++ m2.get.asInstanceOf[Map[String, _]])
    }
    mapCombined.map((e: (String, Any)) => (e._1, primitive2Parameter(e._2)))
  }

  /*The list of hints that tell the parser which parameters to extract*/
  val parameterHints = List(
    classOf[StringParameter],
    classOf[DoubleParameter],
    classOf[IntegerParameter],
    classOf[BooleanParameter],
    classOf[ListParameter],
    classOf[MapParameter],
    classOf[CrossSimpleComponentDescriptor],
    classOf[EvaluatorDescriptor],
    classOf[CollectionReaderDescriptor],
    classOf[SimpleComponentDescriptor],
    classOf[CrossEvaluatorDescriptor],
    classOf[PhaseDescriptor])
  /*The list of all formats that tell the parser which types to extract.
    Append to this list to support new types.*/
  implicit val formats = DefaultFormats.withHints(ShortTypeHints(parameterHints))

  def extract[C](configMap: Map[String, Any])(implicit m: Manifest[C]): C = {
    //Serialize to JSON
    //scala.Map->JSONObject
    //implicit value "formats" is used in decomposition of map into JSONObject.
    val jsonObjConf = decompose(configMap)

    //Deserialize to Scala case classes
    // JSONObj -> ConfigurationDescriptor
    jsonObjConf.extract[C]
  }

  /**
   * Returns a map with all the properties specified of the parent map, merged
   * with the child map. If there is a namespace collision between the child and
   * the parent properties, the child will override the parent.
   *
   * @return a new [[scala.Map]] with all the parent properties merged
   * with the child properties.
   * @param child the child [[scala.Map]]
   * @param parent the parent [[scala.Map]]
   * @param properties the list of properties to be merged
   * @see `joinMaps`
   */

  def mergeWithParent(child: Map[String, _], parent: Map[String, _], properties: List[String]) = ???

  /**
   * Returns string content of the file specified in `in`. Loads file from
   * the classpath using inputstream. Files should be specified in "/path/to/file/myFile"\
   * format.
   *
   * @param in the file specified
   */

  def loadFileContent(in: String, baseDir:String=""): String = {
    println("getFile: " + baseDir + in)
    val contents =
      scala.io.Source.fromFile(baseDir + in).mkString //from filesystem 
      //fromInputStream(getClass.getResourceAsStream(in)).mkString //from classpath
    println("contents: " + contents)
    contents
  }
}