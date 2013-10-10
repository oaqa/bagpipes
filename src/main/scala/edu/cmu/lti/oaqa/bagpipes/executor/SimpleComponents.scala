package edu.cmu.lti.oaqa.bagpipes.executor
import edu.cmu.lti.oaqa.bagpipes.executor._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ComponentDescriptor

trait SimpleComponent extends ExecutableComponent[String]
object SimpleExecutor extends Executor[String, SimpleComponent] {
  override val componentFactory = SimpleComponentFactory
  //need to rethink reset
  def reset(cls: String, a: List[(String, Any)]) = ???
  def getFirstInput = ""
  object SimpleComponentFactory extends ComponentFactory[String, SimpleComponent] {
    def createReader(readerDesc: CollectionReaderDescriptor) = new SimpleReader(readerDesc)
    def createAnnotator(componentDesc: ComponentDescriptor) = new SimpleAnnotator(componentDesc)

    final class SimpleReader(componentDesc: CollectionReaderDescriptor) extends Reader[String] with SimpleComponent {
      def execute(trace: String) = componentDesc.toString
      def destroy = {}
    }
    final class SimpleAnnotator(componentDesc: ComponentDescriptor) extends Annotator[String] with SimpleComponent {
      def execute(trace: String) = trace + "~" + componentDesc
      def destroy = {}
    }
  }
}
