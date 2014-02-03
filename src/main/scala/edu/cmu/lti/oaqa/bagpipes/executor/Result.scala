package edu.cmu.lti.oaqa.bagpipes.executor

import edu.cmu.lti.oaqa.bagpipes.db.BagpipesDatabase.Metric
import edu.cmu.lti.oaqa.bagpipes.annotation.Analytic

case class Result[I](result: I)(implicit analysis: Analytic){
  
  def getAnalysis = analysis
  def getInput =  result
}
  