package edu.cmu.lti.oaqa.bagpipes.executor

import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ComponentDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.AtomicExecutable

trait ComponentFactory[I, C <: ExecutableComponent[I]] {
  def create(execDesc: AtomicExecutable): C = execDesc match {
    case collDesc @ CollectionReaderDescriptor(_, _) => createReader(collDesc)
    case compDesc @ ComponentDescriptor(_, _) => createAnnotator(compDesc)
  }
  def createReader(readerDesc: CollectionReaderDescriptor): Reader[I] with C
  def createAnnotator(componentDesc: ComponentDescriptor): Annotator[I] with C
}
