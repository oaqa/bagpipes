package edu.cmu.lti.oaqa.bagpipes.configuration

import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors._
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST._
import net.liftweb.json.Serialization
import net.liftweb.json.ShortTypeHints
import scala.util.parsing.json.JSONObject
import ParserUtils._
import Implicits._
import edu.cmu.lti.oaqa.bagpipes.configuration.Parameters._
import net.liftweb.json.FullTypeHints
import net.liftweb.json.TypeHints

/**
 * Maps a configuration descriptor (specified as its content or file path) to its canonical [[$confDes]]
 * case class representation as specified in [[$packagePath.ConfigurationDescriptors]].
 *
 * The `Parser` "knows" what parameters (@see [[$packagePath.ConfigurationDescriptors.Parameter]])
 * to use for mapping, by looking at implicit `ShortTypeHints` assigned to
 * `formats`. To support additional parameters, add the new type to the list
 * defined in `parameterHints`.
 *
 * Briefly, parsing configuration descriptors can be visualized as the following \
 * flowchart:
 * (configuration descriptor)->[[java.util.Map]]->[[scala.collections.Map]]->
 * (flattened+converted to Parameters)[[scala.collections.Map]]->[[$confDes]]
 * <p>
 * In more detail:
 * 1. Converts descriptor to [[java.util.Map]] using concrete implementation
 * (e.g., [[$packagePath.YAMLParser]]).
 * <p>
 * 2. Implicitly converts [[java.util.Map]] to [[scala.collections.Map]] using
 * `deepMapAsScalaConverter` and `deepListAsScalaConverter` that recursively convert
 * all the contents of the [[java.util.Map]] to Scala classes.
 * <p>
 * 3. Flattens the configuration map to an _effective_ configuration map. For
 * instance if a configuration descriptor is given as:
 * {{{
 * inherit: edu.cmu.descriptors.foo
 *   params:
 *     param_a: bar
 * }}}
 * if there is another file `edu.cmu.foo`:
 * {{{
 * class: edu.cmu.classes.foo
 *    params:
 *      param_b: baz
 * }}}
 * then the flattened version will result in a map representation
 * {{{
 * class: edu.cmu.classes.foo
 *    params:
 *      param_a: bar
 *      param_b: baz
 * }}}
 * Primitive types are also converted to their corresponding
 * [[$packagePath.ConfigurationDescriptors.Parameter]] types.
 * <p>
 * 4. Converts [[scala.collections.Map]] to [[scala.util.parsing.json.JSONObject]].
 * <p>
 * 5. Extracts [[$packagePath.ConfigurationDescriptors.ConfigurationDescriptor]]
 * from [[scala.util.parsing.json.JSONObject]] using the `extract` method defined
 * in [[net.liftweb.json.Extraction]] and the `ShortTypeHints` passed to `formats`.
 * The extraction is done implicitly by the JSON parsing Lift framework library
 * available from {@link https://github.com/lift/lift/tree/master/framework/lift-base/lift-json/}.
 *
 * @see [[$packagePath.Implicits]]
 * @see [[$packagePath.ConfigurationDescriptors]]
 * @see [[$packagePath.ParserUtils]]
 *
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

trait Parser {

  /**Returns a new [[$confDes]] from parsed contents of a configuration descriptor */
  def parse(resource: String): ConfigurationDescriptor = {
    //YAML->java.util.map->scala.collections.Map
    val resMap = getConfigurationMap(resource)
    parse(resMap)
  }

  /**
   *  Returns a new [[$confDes]] from a parsed [[scala.collections.Map]].
   *
   *  First, the parser "flattens" the map, merging parameters of inheriting components
   *  with their parents, and converting to [[$pacakgePath.ConfigurationDescriptor.Parameter]]
   *  type where necessary. Second, the flattened map is serialized to JSON.
   *  Finally, the serialized JSON is extracted to a new programmatic [[$confDes]]
   *  type using the [[net.liftweb.json.Extraction]] utility from the Lift JSON
   *  library.
   *  @param resMap a map containing the key/value pairs of the descriptor.
   *  @return a new programmatic [[$confDes]] representation of the configuration descriptor.
   */
  private def parse(resMap: Map[String, Any]): ConfigurationDescriptor = {
    //flatten and convert configuration maps to configuration maps with parameters 
    //scala.collections.Map->(flattened+converted to parameters)scala.collections.Map
    val configMap = flattenConfMap(resMap)
    //helpful for debugging
    println("configMap: " + configMap)
    extract[ConfigurationDescriptor](configMap)
  }

  /**
   * Returns a [[scala.util.Map]] representation of a configuration. Reformats
   * file path String to classpath resource style from standard Java package style
   * (e.g.,`edu.cmu.lti.oaqa.cse.configuration` -> `/edu/cmu/lti/oaqa/cse/configuration`).
   *
   * @param path the file path of the configuration descriptor.
   * @return [[scala.util.Map]] representation of the configuration descriptor.
   */
  protected def getConfigurationMapFromPath(path: String): Map[String, Any] = getConfigurationMapFromFile("/" + path.replace(".", "/"))

  /**
   * Returns a new [[scala.util.Map]] representation of a configuration from the
   * configuration descriptor contents. Hook method for concrete implementation
   * (e.g., @see [[$packagePath.YAMLParser]].
   */
  protected def getConfigurationMap(resource: String): Map[String, Any]
  /**
   * Returns a new [[scala.util.Map]] representation of a configuration. Hook method
   * for concrete implementation (e.g., @see [[$packagePath.YAMLParser]].
   */
  protected def getConfigurationMapFromFile(path: String): Map[String, Any]
  /**Returns a new [[$confDes]] from file path of a configuration descriptor */
  def parseFromFile(path: String) = {
    //YAML (filepath)->java.util.map->scala.collections.Map
    val resMap = getConfigurationMapFromFile(path)
    parse(resMap)
  }

  /**
   * Returns a new map from the "flattened," and effective version of the
   * `confMap` where all "inherited" components are substituted by concrete
   * class definitions and parameters are merged. Also, recursively applies
   * `flattenComponent` to any [[scala.collections.Map]] that have an `inherit`
   * key, `flattenMap` to any [[scala.collections.Map]], and `flattenList`
   * to any [[scala.collections.List]] contained in `confMap`.
   *
   * @param  confMap a map representation of the configuration descriptor
   * @return a flattened and effective version of confMap.
   */
  /* private*/ def flattenConfMap(confMap: Map[String, Any]): Map[String, Any] =
    confMap.map {
      case (k, v: List[_]) => (k, flattenConfList(v)) // recursively flatten the list
      case (k, v: Map[_, _]) => (k, flattenComponentOrConfMap(v.asInstanceOf[Map[String, Any]]))
      case (k, v) => (k, v) // do nothing
    }

  /**
   * Returns a new map from the "flattened," and effective version of
   * the `confList` where all "inherited" components are substituted
   * by concrete class definitions and parameters are merged. Also, recursively applies
   * `flattenMap` to any [[scala.collections.Map]], and `flattenList`
   * to any [[scala.collections.List]] contained in `confMap`.
   *
   * @param  confMap a map representation of the configuration descriptor
   * @return a flattened and effective version of confMap.
   */
  private def flattenConfList(confList: List[Any]): List[Any] =
    confList.map {
      case v: List[_] => flattenConfList(v)
      case v: Map[_, _] => flattenComponentOrConfMap(v.asInstanceOf[Map[String, Any]])
      case v => v // do nothing
    }

  /**
   * Returns flattened map representation of a component, if the component
   * contains `inherit` key, or a flattened configuration map. Applies `flattenComponent`
   * if it the configuration map is inheriting from a parent component.
   *
   * @param confMap configuration map containing either an inheriting component or regular map
   * @return a flattened version of the configuration map.
   */
  private def flattenComponentOrConfMap(confMap: Map[String, Any]) = confMap.head match {
    //if it contains "inherit" then flatten the component and combine with inheriting configuration descriptor
    case ("inherit", v: String) => {
      val flattenedConfMap = flattenComponent(confMap, v)
      extractComponent(flattenedConfMap)
    }
    // case ("collection-reader", v: String) => extract[CollectionReaderDescriptor](flattenComponent(confMap,v))
    //else just recursively flatten the map
    case _ => flattenConfMap(confMap)
  }

  def extractComponent(confMap: Map[String, Any]) =
    if (confMap.contains("collection-class")) extract[CollectionReaderDescriptor](confMap)
    else if (confMap.contains("cross-opts"))
      extract[CrossComponentDescriptor](confMap)
    else if (confMap.contains("params")) extract[ComponentDescriptor](confMap)
    else confMap

  /**
   * Returns "flattened," effective version of the component where all "inherits" are
   * substituted by concrete class definitions and parameters are merged. The properties
   * (e.g., "params") to be merged between
   *
   * Example:
   *
   *  * {{{
   * inherit: edu.cmu.descriptors.foo
   *   params:
   *     param_a: bar
   * }}}
   * if there is another file `edu.cmu.foo`:
   * {{{
   * class: edu.cmu.classes.foo
   *    params:
   *      param_b: baz
   * }}}
   * then the flattened version will result in a map representation
   * {{{
   * class: edu.cmu.classes.foo
   *    params:
   *      param_a: bar
   *      param_b: baz
   * }}}
   *
   * @param parentConfMap the parent configuration map
   * @param childResoucePath the path specified by the parent's inherit tag
   */
  private def flattenComponent(parentConfMap: Map[String, Any], childResourcePath: String): Map[String, Any] = {
    val childConfMap = flattenConfMap(getConfigurationMapFromPath(childResourcePath)) // get resource from file specified by head
    // merge parent and child properties (e.g., "params," "cross-opts," etc.)
    val mergedPropertiesConf =
      childConfMap.map {
        // if it's a map merge with parent
        case (k, v: Map[_, _]) => (k -> (joinMaps(childConfMap, parentConfMap, k)))
        // else, just override with child
        case (k, v) => (k -> v)
      }
    // add the rest of the properties in the parent to the merged map
    mergedPropertiesConf ++ flattenConfMap(parentConfMap).filterNot { case (k, v) => childConfMap.contains(k) }
  }

}
  