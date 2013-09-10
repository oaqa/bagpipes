package edu.cmu.lti.oaqa.cse.scala.configuration

object Parameters {
  sealed trait Parameter
  case class IntegerParameter(value: Int) extends Parameter
  case class StringParameter(value: String) extends Parameter
  case class DoubleParameter(value: Double) extends Parameter
  case class BooleanParameter(value: Boolean) extends Parameter
  case class ListParameter(pList: List[Parameter]) extends Parameter
  case class MapParameter(map: Map[String, Parameter]) extends Parameter

  implicit def primitive2Parameter(value: Any): Parameter = value match {
    case v: Int => IntegerParameter(v)
    case v: String => StringParameter(v)
    case v: Double => DoubleParameter(v)
    case v: Boolean => BooleanParameter(v)
    case plist: List[_] => ListParameter(plist.map(p => primitive2Parameter(p)))
    case pmap: Map[_, _] => MapParameter(pmap.map { case (k: String, v) => (k, primitive2Parameter(v)) })
  }

  implicit def listParameter2ParameterList(mapListP: Map[String, ListParameter]) = mapListP.map { case (k, v) => k -> v.pList }
}
