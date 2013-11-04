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
  trait AtomicExecutable extends PipelineDescriptor

  sealed abstract class AtomicExecutableConf(className: String, params: Map[String, Parameter]) extends ExecutableConf(className, params) with AtomicExecutable
  sealed abstract class ExecutableConf(className: String, params: Map[String, Parameter]) extends PipelineDescriptor {
    def getClassName = className
    def getParams = params
    //extends ExecutableDescriptor with ConfExpr
  }

  object AtomicExecutableConf {
    def unapply(aExecConf: AtomicExecutableConf) = Some((aExecConf.getClassName, aExecConf.getParams))
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
  case class ConfigurationDescriptor(configuration: Configuration, `collection-reader`: CollectionReaderDescriptor, pipeline: List[PhaseDescriptor] = Nil)
  case class Configuration(name: String = "default-config", author: String = "default-author")
  case class ComponentDescriptor(`class`: String, params: Map[String, Parameter] = Map()) extends AtomicExecutableConf(`class`, params) {
    // need to expiicitly declare constructors with default arguments to make lift-json extraction happy
    def this(`class`: String) = this(`class`, Map())
  }
  case class CollectionReaderDescriptor(`collection-class`: String, params: Map[String, Parameter] = Map()) extends AtomicExecutableConf(`collection-class`, params) with PipelineDescriptor {
    // need to expiicitly declare constructors with default arguments to make lift-json extraction happy
    def this(`collection-class`: String) = this(`collection-class`, Map())
  }
  case class PhaseDescriptor(phase: String, options: List[AtomicExecutableConf]) extends PipelineDescriptor
  case class CrossComponentDescriptor(`class`: String, params: Map[String, Parameter] = Map(), `cross-opts`: Map[String, ListParameter] = Map()) extends AtomicExecutableConf(`class`, params) {
    // need to expiicitly declare constructors with default arguments to make lift-json extraction happy
    def this(`class`: String, `cross-opts`: Map[String, ListParameter]) = this(`class`, Map(), `cross-opts`)
  }

}
