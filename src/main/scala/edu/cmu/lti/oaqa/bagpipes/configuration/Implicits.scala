package edu.cmu.lti.oaqa.bagpipes.configuration
import scala.collection.JavaConverters._
import net.liftweb.json.JsonAST._
import scala.collection.JavaConversions._
import net.liftweb.json.Serialization
import net.liftweb.json.ShortTypeHints
import edu.cmu.lti.oaqa.bagpipes.configuration._

/**
 * Some implicit methods to help convert from Java types to Scala types, and
 * to retrieve typesafe parameters from a heterogeneous map.
 * 
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

object Implicits {
  class MapWithRestrictTo[A, That](from: Map[A, That]) {
    def restrictTo[B](implicit m: scala.reflect.Manifest[B]): Map[A, B] = {
      from.filter((e: (A, That)) => m.erasure.isInstance(e._2))
        .map((e: (A, That)) => e.asInstanceOf[(A, B)])
    }
  }
  implicit def makeMapWithRestrictTo[A, B](from: Map[A, B]) = {
    new MapWithRestrictTo(from)
  }

  /**
   * Implicitly returns a Scala map converted from a Java map. If the
   * values of the Java map contains any references to another Java map or
   * list, then those will be recursively converted to Scala map or list
   * respectively. If one of the keys in the map uses the Java keyword "class"
   * then that will be converted to className so as to be compatible with
   * the Configuration case classes.
   */
  implicit def deepMapAsScalaConverter[A, B](javaMap: java.util.Map[A, B]): Map[String, Any] = {
    def deepMapAsScalaConverter[A, B](javaMap: java.util.Map[A, B]): Map[String, Any] =
      javaMap.toMap.map {
        case (k: String, v: java.util.Map[_, _]) => (k, deepMapAsScalaConverter(v))
        case (k: String, v: java.util.List[_]) => (k, deepListAsScalaConverter(v))
        case (k: String, v) => (k, v)
      }
    deepMapAsScalaConverter(javaMap)
  }

  /**
   * Implicitly returns a Scala list and converted it to a Java list. If the
   * values of the Java list contains any references to another Java map or
   * list, then those will be recursively converted to Scala map or list
   * respectively.
   */
  implicit def deepListAsScalaConverter[A](list: java.util.List[A]): List[Any] = {
    def deepListAsScalaConverter[A](list: java.util.List[A]): List[Any] =
      list.toList.map {
        case v: java.util.Map[_, _] => deepMapAsScalaConverter(v)
        case v: java.util.List[_] => deepListAsScalaConverter(v)
        case v => v
      }
    deepListAsScalaConverter(list)
  }
}

 