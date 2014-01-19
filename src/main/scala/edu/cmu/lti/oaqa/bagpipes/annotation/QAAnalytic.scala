package edu.cmu.lti.oaqa.bagpipes.annotation

import edu.cmu.lti.oaqa.bagpipes.db.BagpipesDatabase.Metric

case class QAAnalytic(qaSets: List[AnalysisQASet] = Nil, neList: List[NamedEntity] = Nil,
  candSents: List[AnalysisCandidateSentence] = Nil, metrics: List[AnalysisMetric] = Nil) extends Analytic(metrics)

sealed abstract class Annotation[T <: Annotation[T]](begin: Int, end: Int, text: String) extends AnalysisType {
  def getBegin = begin
  def getEnd = end
  def getText = text
  private def getLength = text.length
  def contains(thatAnnot: T): Boolean =
    if (getLength > thatAnnot.getLength)
      this.text.contains(thatAnnot.getText)
    else thatAnnot.getText.contains(this.getText)

  //  def getFeatures = features
}

object Annotation {
  def unapply(annot: Annotation[_]) = Some((annot.getBegin, annot.getEnd, annot.getText))
}

case class Evaluations[T <: Annotation[T]](map: Map[AnalysisQASet, (List[T], List[T], Double)])
//case class Evaluation[T <: Annotation[T]](candList: List[T], testList: List[T])


case class AnalysisDocument(begin: Int, end: Int, text: String, id: String,
  filteredText: String, sentList: List[AnalysisSentence])
  extends Annotation(begin, end, text)

case class AnalysisSentence(begin: Int, end: Int, text: String, id: String,
  qualityScore: Double, depList: List[AnalysisDependency], tokLisg: List[AnalysisToken],
  bFilter: Boolean, phraseList: List[NP], nerList: List[NamedEntity])
  extends Annotation(begin, end, text)

case class AnalysisAnswer(begin: Int, end: Int, text: String, id: String,
  questionId: String, docId: String, synonyms: List[AnalysisSynonym], isCorrect: Boolean,
  isSelected: Boolean, npList: List[NP], nerList: List[NamedEntity],
  tokList: List[AnalysisToken], depList: List[AnalysisDependency])
  extends Annotation(begin, end, text)

case class AnalysisQASet(begin: Int, end: Int, text: String,
  answers: List[AnalysisAnswer], candidateSentences: List[AnalysisCandidateSentence])
  extends Annotation(begin, end, text)

case class AnalysisCandidateSentence(
  begin: Int, end: Int,
  relevance: Double,
  sentence: AnalysisSentence, depMatch: Double, synMatch: Double, candAnswer: List[AnalysisCandidateAnswer]) extends Annotation(begin, end, "")

case class AnalysisCandidateAnswer(begin: Int, end: Int, text: String, qId: String,
  choiceIdx: Int, pmi: Double, simScore: Double, synScore: Double)
  extends Annotation(begin, end, text)

case class AnalysisQuestion(begin: Int, end: Int, text: String, id: String,
  dependencies: List[AnalysisDependency], nerList: List[NamedEntity],
  nounList: List[NP], tokList: List[AnalysisToken])
  extends Annotation(begin, end, text)

case class NP(begin: Int, end: Int, text: String,
  weight: Double, synonyms: List[AnalysisSynonym]) extends Annotation(begin, end, text)

case class NamedEntity(begin: Int, end: Int, text: String, tag: String,
  weight: Double, synList: List[AnalysisSynonym]) extends Annotation[NamedEntity](begin, end, text)

case class AnalysisToken(begin: Int, end: Int, pos: String, ner: String, text: String)
  extends Annotation(begin, end, text)

case class AnalysisSynonym(begin: Int, end: Int, text: String, weight: Double,
  source: String) extends Annotation(begin, end, text)

case class AnalysisDependency(begin: Int, end: Int, governer: AnalysisToken,
  dependent: AnalysisToken, relation: String) extends Annotation(begin, end, "") 
