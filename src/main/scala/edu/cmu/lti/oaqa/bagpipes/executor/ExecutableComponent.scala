package edu.cmu.lti.oaqa.bagpipes.executor

import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ComponentDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor

abstract trait Reader[I] 

abstract trait Annotator[I] 

trait ExecutableComponent[I] {
  def execute(input: I): I
  def destroy()
}

