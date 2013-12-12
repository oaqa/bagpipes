package edu.cmu.lti.oaqa.bagpipes.executor

import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.EvaluatorDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.SimpleComponentDescriptor

trait ComponentFactory[I, C <: ExecutableComponent[I]] {
  def create(execDesc: AtomicExecutable): C = execDesc match {
    case collDesc @ CollectionReaderDescriptor(_, _) => createReader(collDesc)
    case compDesc @ SimpleComponentDescriptor(_, _) => createAnnotator(compDesc)
    case evalDesc @ EvaluatorDescriptor(_, _) => createEvaluator(evalDesc)
  }
  def createReader(readerDesc: CollectionReaderDescriptor): Reader[I] with C
  def createAnnotator(componentDesc: ComponentDescriptor): Annotator[I] with C
  def createEvaluator[T](evalDesc: EvaluatorDescriptor): Evaluator[I,T] with C
}
