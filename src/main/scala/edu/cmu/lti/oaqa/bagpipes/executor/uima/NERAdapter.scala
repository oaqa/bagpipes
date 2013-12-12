package edu.cmu.lti.oaqa.bagpipes.executor.uima

import edu.cmu.lti.oaqa.bagpipes.executor.Adapter
import org.apache.uima.jcas.JCas
import edu.cmu.lti.oaqa.bagpipes.annotation.NamedEntity
import org.apache.uima.fit.util.JCasUtil
import edu.cmu.lti.oaqa.bagpipes.annotation.temp.types.NER
import scala.collection.JavaConversions._
import org.apache.uima.jcas.cas.FSList
import java.util.Collection
import org.apache.uima.jcas.cas.TOP
import scala.collection.JavaConversions._
class NERAdapter extends Adapter[JCas, NamedEntity] {

  implicit def ner2NamedEntity(ner: NER): NamedEntity =
    NamedEntity(ner.getBegin(), ner.getEnd(), ner.getText(), ner.getSource(), ner.getWeight(), Nil)

  implicit def fsList2Collection[T <: TOP](fsList: FSList, cls: Class[T]): List[T] =
    JCasUtil.select(fsList, cls).toList

  override def getCandidates(input: JCas): List[NamedEntity] = {
    val namedEntities = JCasUtil.select(input, classOf[NER]).toList.map(ne => ???)
    ???
  }

}