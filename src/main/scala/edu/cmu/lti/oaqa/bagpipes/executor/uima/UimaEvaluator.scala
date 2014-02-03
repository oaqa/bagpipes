package edu.cmu.lti.oaqa.bagpipes.executor.uima

import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.EvaluatorDescriptor
import org.apache.uima.jcas.JCas
import edu.cmu.lti.oaqa.bagpipes.executor.Evaluator
import edu.cmu.lti.oaqa.bagpipes.executor.Adapter
import edu.cmu.lti.oaqa.bagpipes.executor.Result
import edu.cmu.lti.oaqa.bagpipes.annotation.QAAnalytic
import edu.cmu.lti.oaqa.bagpipes.annotation.NamedEntity
import edu.cmu.lti.oaqa.bagpipes.annotation._
import scala.math._
import UimaEvaluator._
import edu.cmu.lti.oaqa.bagpipes.db.BagpipesDatabase.Metric
class UimaEvaluator[T](evalDesc: EvaluatorDescriptor) extends UimaComponent(evalDesc) with Evaluator[JCas, T] {
  val EvaluatorDescriptor(_, dParams) = evalDesc
  def executeComponent(input: Result[JCas]): Result[JCas] = {
    val result = input
    val analytic @ QAAnalytic(qaSets, nes, cands, metrics) = result.getAnalysis

    /*extracting candidates and multiple-choice answers*/
    val evals = for {
      qaSet <- qaSets
      candidate <- qaSet.candidateSentences.map(cs => cs.sentence)
      answer <- qaSet.answers
    } yield {
      //getGS
      val answerNers: List[NamedEntity] = answer.nerList
      //getCandidates
      val candidateNers: List[NamedEntity] = candidate.nerList
      (qaSet, answer, answerNers, candidateNers)
    }

    /* processing for convenience (grouping by qa sets)*/
    val groupedEvals = evals.groupBy { case (qaSet, _, _, _) => qaSet }

    /* Scoring phase*/
    val scoredEvals = groupedEvals.map {
      case (_, qaSet) => scoreQASet(qaSet.map(x => (x._2, x._3, x._4)), dParams("n").asInstanceOf[Int])
    }

    /*Voting/merging phase */
    val votedEvals = scoredEvals.map(_.maxBy(_._2))

    votedEvals.foreach {
      case (answer, _) =>
        println("selected answer: " + answer + " is " + answer.isCorrect)
    }

    /* computing metric against gold standard */
    val matched: Double = votedEvals.count { case (answer, _) => answer.isCorrect }

    val total: Double = votedEvals.size

    val unanswered = 1.0d // hard-coded for now

    val cAt1 = matched / total * unanswered + matched * (1.0d / total)

    Result(result.getInput)(QAAnalytic(qaSets, nes, cands, AnalysisMetric("cAt1", cAt1) :: Nil))
  }

  def destroy(): Unit = ???

}

object UimaEvaluator {

  def scoreQASet[T <: Annotation[T]](qaSet: List[(AnalysisAnswer, List[T], List[T])], n: Int): List[(AnalysisAnswer, Int)] = qaSet.map(x => (x._1, ngramMatch(x._2, x._3, n)))

  def ngramMatch[T <: Annotation[T]](candList: List[T], testList: List[T], n: Int): Int = {
    val maxN = min(n, max(candList.length, testList.length))
    val canGrams = genGrams(candList, maxN)
    val testGrams = genGrams(testList, maxN)
    val matches = for {
      cGram <- canGrams
      tGram <- testGrams
    } yield {
      matchGrams(cGram, tGram)
    }
    matches.count(x => x)
  }

  def matchGrams[T <: Annotation[T]](candGrams: List[T], testGrams: List[T]): Boolean = candGrams match {
    case Nil => true
    case head :: tail => head.contains(testGrams.head) && matchGrams(tail, testGrams.tail)
  }

  def genGrams[T](grams: List[T], n: Int, acc: List[T] = Nil): List[List[T]] = (grams, n, acc) match {
    case (Nil, _, _) => Nil
    case (head :: tail, 0, acc) => List(acc)
    case (head :: tail, n, acc) => genGrams(tail, n - 1, acc ::: head :: Nil) ::: genGrams(tail, n)
  }

}
