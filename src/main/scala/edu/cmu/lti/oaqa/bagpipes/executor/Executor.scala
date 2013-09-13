package edu.cmu.lti.oaqa.bagpipes.executor

/**
 * @param xT
 * 		Data exchange type
 * @param cT
 * 		ExecutableComponent parent type
 */
trait Executor[xT, cT] {
  def execute(cls: String, params: List[(String,Any)], trace: String)
  def reset(cls: String, params: List[(String, Any)]): Boolean
}