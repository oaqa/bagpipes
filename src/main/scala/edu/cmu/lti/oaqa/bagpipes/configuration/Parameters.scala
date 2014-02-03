package edu.cmu.lti.oaqa.bagpipes.configuration

import java.util.Arrays
import java.util.HashMap

/**
 * Case classes for all the parameters used in the configuration descriptors.
 *
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

object Parameters {
  sealed abstract class Parameter(elem: Any) {
    def getElem = elem
  }
  case class IntegerParameter(value: Int) extends Parameter(value)
  case class StringParameter(value: String) extends Parameter(value)
  case class DoubleParameter(value: Double) extends Parameter(value)
  case class BooleanParameter(value: Boolean) extends Parameter(value)
  case class ListParameter(pList: List[Parameter]) extends Parameter(pList)
  // case class FloatParameter(value: Float) extends Parameter(value)
  case class MapParameter(map: Map[String, Parameter]) extends Parameter(map)

  implicit def primitive2Parameter(value: Any): Parameter = value match {
    case v: Int => IntegerParameter(v)
    case v: String => StringParameter(v)
    //case v: Float => FloatParameter(v)
    case v: Double => DoubleParameter(v)
    case v: Boolean => BooleanParameter(v)
    case plist: List[_] => ListParameter(plist.map(p => primitive2Parameter(p)))
    case pmap: Map[_, _] => MapParameter(pmap.map { case (k: String, v) => (k, primitive2Parameter(v)) })
  }

}
