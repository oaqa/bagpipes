package edu.cmu.lti.oaqa.bagpipes.executor
import edu.cmu.lti.oaqa.bagpipes.executor._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.EvaluatorDescriptor
import edu.cmu.lti.oaqa.bagpipes.annotation.QAAnalytic

trait SimpleComponent extends ExecutableComponent[String]
/**
 * Simple example of how to implement an Executor. This executor simply appends
 * the most recent component descriptor to a running subtrace.Thus, running any
 * component returns the "root-to-node" path of that component in the explored
 * configuration space.
 *
 * This class is also intended for designing simple unit-tests.
 *
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

object SimpleExecutor extends Executor[String, SimpleComponent] {
  override val componentFactory = SimpleComponentFactory
  //need to rethink reset
  def reset(cls: String, a: List[(String, Any)]) = ???
  def getFirstInput = Result("")(QAAnalytic())
  object SimpleComponentFactory extends ComponentFactory[String, SimpleComponent] {
    def createReader(readerDesc: CollectionReaderDescriptor) = new SimpleReader(readerDesc)
    def createAnnotator(componentDesc: ComponentDescriptor) = new SimpleAnnotator(componentDesc)
    def createEvaluator[T](evalDesc: EvaluatorDescriptor) = new SimpleEvaluator(evalDesc)
    final class SimpleReader(componentDesc: CollectionReaderDescriptor) extends Reader[String] with SimpleComponent {
      def execute(trace: Result[String]) = Result(trace.result + componentDesc.toString)(trace.getAnalysis)
      override def getTotalInputs = 2
      def destroy = {}
    }
    final class SimpleAnnotator(componentDesc: ComponentDescriptor) extends Annotator[String] with SimpleComponent {
      def execute(trace: Result[String]) = Result(trace.result + "~" + componentDesc)(trace.getAnalysis)
      def destroy = {}
    }

    final class SimpleEvaluator[T](componentDesc: EvaluatorDescriptor) extends Evaluator[String, T] with SimpleComponent {

      def execute(trace: Result[String]) = Result(trace.result + "~" + componentDesc)(trace.getAnalysis)
      def destroy = {}
    }
  }
}
