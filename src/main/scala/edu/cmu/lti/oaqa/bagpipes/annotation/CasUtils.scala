package edu.cmu.lti.oaqa.bagpipes.annotation

import org.apache.uima.jcas.JCas
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.NER
import org.apache.uima.jcas.cas.TOP
import org.apache.uima.fit.util.JCasUtil
import org.apache.uima.jcas.cas.FSList
import scala.collection.JavaConversions._
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.SourceDocument
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.Sentence
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.Answer
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.QuestionAnswerSet
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.CandidateSentence
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.NounPhrase
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.Token
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.Synonym
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.Dependency
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.CandidateAnswer
object CasUtils {
  //type conversions
  implicit def sourceDoc2AnalysisDoc(sDoc: SourceDocument): AnalysisDocument =
    AnalysisDocument(sDoc.getBegin(), sDoc.getEnd(), sDoc.getText(), sDoc.getId(),
      sDoc.getFilteredText(), fsList2List(sDoc.getSentenceList(), classOf[Sentence]))

  implicit def sentence2AnalysisSentence(sent: Sentence): AnalysisSentence =
    AnalysisSentence(sent.getBegin(), sent.getEnd(), sent.getText(), sent.getId(),
      sent.getQualityScore(), fsList2List(sent.getDependencyList(), classOf[Dependency]),
      fsList2List(sent.getTokenList(), classOf[Token]), sent.getBFilter(),
      fsList2List(sent.getPhraseList(), classOf[NounPhrase]),
      fsList2List(sent.getNerList(), classOf[NER]))

  implicit def answer2AnalyisAnswer(ans: Answer): AnalysisAnswer =
    AnalysisAnswer(ans.getBegin(), ans.getEnd(), ans.getText(), ans.getId(),
      ans.getQuestionId(), ans.getDocId(), fsList2List(ans.getSynonyms(),
        classOf[Synonym]), ans.getIsCorrect(), ans.getIsSelected(),
      fsList2List(ans.getNounPhraseList(), classOf[NounPhrase]),
      fsList2List(ans.getNerList(), classOf[NER]),
      fsList2List(ans.getTokenList(), classOf[Token]),
      fsList2List(ans.getDependencies(), classOf[Dependency]))

  implicit def qaSet2AnalysisQASet(qaSet: QuestionAnswerSet): AnalysisQASet =
    AnalysisQASet(qaSet.getBegin(), qaSet.getEnd(), "",
      fsList2List(qaSet.getAnswerList(), classOf[Answer]),
      fsList2List(qaSet.getCandidateSentenceList(),
        classOf[CandidateSentence]))

  implicit def candSent2AnalysisCandidateSent(candSent: CandidateSentence): AnalysisCandidateSentence =
    AnalysisCandidateSentence(
      candSent.getBegin(),
      candSent.getEnd(),
      candSent.getRelevanceScore(),
      candSent.getSentence(),
      candSent.getDepMatchScore(),
      candSent.getSynonymMatchScore(),
      fsList2List(candSent.getCandAnswerList(), classOf[CandidateAnswer]))

  implicit def candAns2AnalysisCandAns(candAns: CandidateAnswer): AnalysisCandidateAnswer =
    AnalysisCandidateAnswer(candAns.getBegin(), candAns.getEnd(),
      candAns.getText(), candAns.getQId(), candAns.getChoiceIndex(),
      candAns.getPMIScore(), candAns.getSimilarityScore(),
      candAns.getSynonymScore())

  implicit def nounPhrase2NP(np: NounPhrase): NP =
    NP(np.getBegin(), np.getEnd(), np.getText(), np.getWeight(),
      fsList2List(np.getSynonyms(), classOf[Synonym]))

  implicit def tok2AnalysisToken(tok: Token): AnalysisToken =
    AnalysisToken(tok.getBegin(), tok.getEnd(), tok.getPos(),
      tok.getNer(), tok.getText())

  implicit def ner2NamedEntity(ner: NER): NamedEntity =
    NamedEntity(ner.getBegin(), ner.getEnd(), ner.getText(),
      ner.getSource(), ner.getWeight(), Nil)

  implicit def syn2AnalysisSyn(syn: Synonym): AnalysisSynonym =
    AnalysisSynonym(syn.getBegin(), syn.getEnd(), syn.getText(), syn.getWeight(), syn.getSource())

  implicit def dep2AnalysisDependency(dep: Dependency): AnalysisDependency =
    AnalysisDependency(dep.getBegin(), dep.getEnd(), dep.getGovernor(),
      dep.getDependent(), dep.getRelation())

  implicit def fsList2List[T <: TOP, A <: AnalysisType](fsList: FSList, cls: Class[T])(implicit f: (T => A)): List[A] =
    JCasUtil.select(fsList, cls).toList.map((t: T) => f(t))

  implicit def cas2List[T <: TOP, A <: AnalysisType](cas: JCas, cls: Class[T])(implicit f: (T => A)): List[A] =
    JCasUtil.select(cas, cls).toList.map((t: T) => f(t))

  implicit def cas2analysis(cas: JCas): QAAnalytic = {
    val qaSets = cas2List(cas, classOf[QuestionAnswerSet])
    val namedEntities = cas2List(cas, classOf[NER])
    val candSents = cas2List(cas, classOf[CandidateSentence])
    //...
    QAAnalytic(qaSets, namedEntities, candSents, Nil)
  }

}