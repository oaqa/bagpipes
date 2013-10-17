package edu.cmu.lti.oaqa.bagpipes.executor

import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ComponentDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor

abstract trait Reader[I] {
  //def hasNext() : Boolean
  //def getNext(input: I) : Unit
  def getTotalInputs() : Int
}

abstract trait Annotator[I] 

trait ExecutableComponent[I] {
  def execute(input: I): I
  def destroy()
}

