package edu.cmu.lti.oaqa.bagpipes.configuration

import java.util.Map.Entry
import Implicits._
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonAST.JInt
import net.liftweb.json.JsonAST.JString
import net.liftweb.json.JsonAST.JBool
import net.liftweb.json.JsonAST.JDouble
import Parameters.Parameter
import edu.cmu.lti.oaqa.bagpipes.configuration.Parameters._

/**
 * All the case classes needed for a configuration descriptor.
 *
 * ConfigurationDescriptor: top-level descriptor
 * Configuration: name and author metadata
 * CollectionReaderDescriptor: the class and parameters for the collection reader
 * PhaseDescriptor: metadata for which options will be executed in this phase
 * ComponentDescriptor: class and parameter for any executable annotator
 * CrossComponentDescriptor: metadata for which parameters will be combined in this `cross-opts`
 * ScoreDescriptor: //yet-to-be implemented
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

object Descriptors {

  trait PipelineDescriptor
  trait AtomicExecutable

  sealed abstract class AtomicExecutableConf(className: String, params: Map[String, Parameter]) extends ExecutableConf(className, params) with AtomicExecutable
  sealed abstract class ExecutableConf(className: String, params: Map[String, Parameter]) extends PipelineDescriptor {
    def getClassName = className
    def getParams = params
    //extends ExecutableDescriptor with ConfExpr
  }
  /**
   * Companion object for pattern-matching to distinguish between general
   * `PipelineDescriptor` and `ExecutableConf`.
   */
  object ExecutableConf extends PipelineDescriptor {
    //To be used for pattern-matching
    def unapply(execConf: ExecutableConf): Option[(String, Map[String, Parameter])] = Some(execConf.getClassName, execConf.getParams)
  }

  sealed abstract class ExecutableComponent(className: String, params: Map[String, Parameter]) extends ExecutableConf(className: String, params: Map[String, Parameter])
  case class ConfigurationDescriptor(configuration: Configuration, `collection-reader`: CollectionReaderDescriptor, pipeline: List[PhaseDescriptor])
  case class Configuration(name: String = "default-config", author: String = "default-author")
  case class CollectionReaderDescriptor(`class`: String, params: Map[String, Parameter] = Map()) extends AtomicExecutableConf(`class`, params)
  case class PhaseDescriptor(phase: String, options: List[ExecutableConf]) extends PipelineDescriptor
  case class ComponentDescriptor(`class`: String, params: Map[String, Parameter] = Map()) extends AtomicExecutableConf(`class`, params)
  case class CrossComponentDescriptor(`class`: String, params: Map[String, Parameter] = Map(), `cross-opts`: Map[String, ListParameter] = Map()) extends ExecutableConf(`class`, params)
}
/*
sealed abstract class ParameterizedDescriptor(`class`: String, parmeters: Map[String, Any]) {
  def get[T](key: String)(implicit m: scala.reflect.Manifest[T]): Option[T] =
    parmeters.restrictTo[T].get(key) match {
      case None => { println("key: " + key + " of type: " + m.erasure + " does not exist!"); None }
      case s => s
    }
  def get(key: String) = get[Any](key)
  //typesafe get methods, guaranteed to get a parameter of a given type 
  // or nothing if it is not found
  def getInt(key: String) = get[IntegerParameter](key)
  def getDouble(key: String) = get[DoubleParameter](key)
  def getString(key: String) = get[StringParameter](key)
  def getBoolean(key: String) = get[BooleanParameter](key)
  def getMap(key: String) = get[MapParameter](key)
  def getList(key: String) = get[ListParameter](key)
}*/