package edu.cmu.lti.oaqa.bagpipes.executor

abstract class ExecutableComponent[T] {
  def execute(input: T): Any
  def destroy()
}