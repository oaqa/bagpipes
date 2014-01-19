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
  import AbstractDescriptors._

  case class ConfigurationDescriptor(configuration: Configuration, `collection-reader`: CollectionReaderDescriptor, pipeline: List[PipelineDescriptor] = Nil)
  case class Configuration(name: String = "default-config", author: String = "default-author")
  case class SimpleComponentDescriptor(`class`: String, params: Map[String, Parameter] = Map()) extends ComponentDescriptor(`class`, params) with PipelineDescriptor {
    def this(`class`: String) = this(`class`: String, Map())
  }

  case class CollectionReaderDescriptor(`collection-class`: String, params: Map[String, Parameter] = Map()) extends AtomicExecutableConf(`collection-class`, params) with PipelineDescriptor {
    // need to expiicitly declare constructors with default arguments to make lift-json extraction happy
    def this(`collection-class`: String) = this(`collection-class`, Map())
  }
  case class PhaseDescriptor(phase: String, options: List[AtomicExecutableConf]) extends PipelineDescriptor

  case class CrossEvaluatorDescriptor(evaluator: String, params: Map[String, Parameter], `cross-opts`: Map[String, ListParameter]) extends CrossComponentDescriptor(evaluator, params, `cross-opts`) {
    def this(evaluator: String, `cross-opts`: Map[String, ListParameter]) = this(evaluator, Map(), `cross-opts`)
    override def getComponent(name: String, params: Map[String, Parameter]): AtomicExecutableConf = EvaluatorDescriptor(name, params)
  }

  case class CrossSimpleComponentDescriptor(`class`: String, params: Map[String, Parameter], `cross-opts`: Map[String, ListParameter]) extends CrossComponentDescriptor(`class`, params, `cross-opts`) {
    def this(`class`: String, `cross-opts`: Map[String, ListParameter]) = this(`class`, Map(), `cross-opts`)
    override def getComponent(name: String, params: Map[String, Parameter]): AtomicExecutableConf = SimpleComponentDescriptor(name, params)
  }

  case class EvaluatorDescriptor(evaluator: String, params: Map[String, Parameter]) extends ComponentDescriptor(evaluator, params)
}

object AbstractDescriptors {

  trait PipelineDescriptor
  trait AtomicExecutable extends PipelineDescriptor
  sealed abstract trait Expandable {
    def expand: Stream[AtomicExecutableConf]
  }

  sealed abstract class AtomicExecutableConf(className: String, params: Map[String, Parameter]) extends ExecutableConf(className, params) with AtomicExecutable
  sealed abstract class ExecutableConf(className: String, params: Map[String, Parameter]) extends PipelineDescriptor {
    def getClassName = className
    def getParams = params
    //extends ExecutableDescriptor with ConfExpr
  }

  sealed abstract class CrossComponentDescriptor(`class`: String, params: Map[String, Parameter] = Map(), `cross-opts`: Map[String, ListParameter] = Map()) extends AtomicExecutableConf(`class`, params) with Expandable with PipelineDescriptor {
    // need to expiicitly declare constructors with default arguments to make lift-json extraction happy
    def this(`class`: String, `candidate-provider`: String, `gs-provider`: String, `cross-opts`: Map[String, ListParameter]) = this(`class`, Map(), `cross-opts`)
    def getCrossParams = `cross-opts`
    def getComponent(name: String, params: Map[String, Parameter]): AtomicExecutableConf
    override def expand: Stream[AtomicExecutableConf] = {
      def expandParams(cPMap: Map[String, List[Parameter]]): Stream[Map[String, Parameter]] =
        cPMap.keys.toList match {
          case Nil => Stream(Map())
          case headKey :: tailKeys => {
            for {
              p <- cPMap(headKey).toStream
              pMap <- expandParams(cPMap.tail.toMap)
            } yield Map(headKey -> p) ++ pMap
          }
        }
      for (cParams <- expandParams(`cross-opts`.flatMap { case (k, v) => Map(k -> v.pList) }))
        yield getComponent(`class`, params ++ cParams)
    }
  }
  
  

  abstract sealed class ComponentDescriptor(`class`: String, params: Map[String, Parameter] = Map()) extends AtomicExecutableConf(`class`, params) {
    // need to expiicitly declare constructors with default arguments to make lift-json extraction happy
    def this(`class`: String) = this(`class`, Map())
  }

  object CrossComponentDescriptor {
    def unapply(ccDesc: CrossComponentDescriptor) = Some(ccDesc.getClassName, ccDesc.getParams, ccDesc.getCrossParams)
  }

  object AtomicExecutableConf {
    def unapply(aExecConf: AtomicExecutableConf) = Some((aExecConf.getClassName, aExecConf.getParams))
  }

  object ComponentDescriptor {
    def unapply(compDesc: ComponentDescriptor) = Some((compDesc.getClassName, compDesc.getParams))
  }
  /**
   * Companion object for pattern-matching to distinguish between general
   * `PipelineDescriptor` and `ExecutableConf`.
   */
  object ExecutableConf extends PipelineDescriptor {
    //To be used for pattern-matching
    def unapply(execConf: ExecutableConf) = Some(execConf.getClassName, execConf.getParams)
  }

  sealed abstract class ExecutableComponent(className: String, params: Map[String, Parameter]) extends ExecutableConf(className: String, params: Map[String, Parameter])

}
