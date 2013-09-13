package edu.cmu.lti.oaqa.bagpipes.executor

import org.apache.uima.analysis_component.AnalysisComponent_ImplBase

class SimpleExec extends Executor[String, ExecutableComponent[String]] {
  
  var count = 0
  
  override def execute(cls: String, params: List[(String, Any)], trace: String) = {
    println("Executing component: "+cls)
    println("    with parameters: "+params.toString())
    println("    in context: "+trace)
  }
  
  def reset(cls: String, params: List[(String, Any)]): Boolean = {
    println("Executing (final) component: "+cls)
    println("    with parameters: "+params.toString())
    count += 1
    count <= 3
  }
}