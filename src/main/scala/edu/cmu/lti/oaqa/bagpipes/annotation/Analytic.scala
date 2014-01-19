package edu.cmu.lti.oaqa.bagpipes.annotation

trait AnalysisType
abstract class Analytic(metric: List[AnalysisMetric])
case class SimpleAnalytic(metric: List[AnalysisMetric]) extends Analytic(metric)
case class AnalysisMetric(name: String, score: Double) extends AnalysisType
