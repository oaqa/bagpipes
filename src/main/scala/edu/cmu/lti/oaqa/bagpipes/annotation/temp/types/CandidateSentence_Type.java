
/* First created by JCasGen Mon Mar 25 03:46:07 EDT 2013 */
package edu.cmu.lti.oaqa.bagpipes.annotation.temp.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;

import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Mon May 13 14:41:33 EDT 2013
 * @generated */
public class CandidateSentence_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CandidateSentence_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CandidateSentence_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CandidateSentence(addr, CandidateSentence_Type.this);
  			   CandidateSentence_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CandidateSentence(addr, CandidateSentence_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = CandidateSentence.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.lti.qalab.types.CandidateSentence");
 
  /** @generated */
  final Feature casFeat_relevanceScore;
  /** @generated */
  final int     casFeatCode_relevanceScore;
  /** @generated */ 
  public double getRelevanceScore(int addr) {
        if (featOkTst && casFeat_relevanceScore == null)
      jcas.throwFeatMissing("relevanceScore", "edu.cmu.lti.qalab.types.CandidateSentence");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_relevanceScore);
  }
  /** @generated */    
  public void setRelevanceScore(int addr, double v) {
        if (featOkTst && casFeat_relevanceScore == null)
      jcas.throwFeatMissing("relevanceScore", "edu.cmu.lti.qalab.types.CandidateSentence");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_relevanceScore, v);}
    
  
 
  /** @generated */
  final Feature casFeat_sentence;
  /** @generated */
  final int     casFeatCode_sentence;
  /** @generated */ 
  public int getSentence(int addr) {
        if (featOkTst && casFeat_sentence == null)
      jcas.throwFeatMissing("sentence", "edu.cmu.lti.qalab.types.CandidateSentence");
    return ll_cas.ll_getRefValue(addr, casFeatCode_sentence);
  }
  /** @generated */    
  public void setSentence(int addr, int v) {
        if (featOkTst && casFeat_sentence == null)
      jcas.throwFeatMissing("sentence", "edu.cmu.lti.qalab.types.CandidateSentence");
    ll_cas.ll_setRefValue(addr, casFeatCode_sentence, v);}
    
  
 
  /** @generated */
  final Feature casFeat_depMatchScore;
  /** @generated */
  final int     casFeatCode_depMatchScore;
  /** @generated */ 
  public double getDepMatchScore(int addr) {
        if (featOkTst && casFeat_depMatchScore == null)
      jcas.throwFeatMissing("depMatchScore", "edu.cmu.lti.qalab.types.CandidateSentence");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_depMatchScore);
  }
  /** @generated */    
  public void setDepMatchScore(int addr, double v) {
        if (featOkTst && casFeat_depMatchScore == null)
      jcas.throwFeatMissing("depMatchScore", "edu.cmu.lti.qalab.types.CandidateSentence");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_depMatchScore, v);}
    
  
 
  /** @generated */
  final Feature casFeat_synonymMatchScore;
  /** @generated */
  final int     casFeatCode_synonymMatchScore;
  /** @generated */ 
  public double getSynonymMatchScore(int addr) {
        if (featOkTst && casFeat_synonymMatchScore == null)
      jcas.throwFeatMissing("synonymMatchScore", "edu.cmu.lti.qalab.types.CandidateSentence");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_synonymMatchScore);
  }
  /** @generated */    
  public void setSynonymMatchScore(int addr, double v) {
        if (featOkTst && casFeat_synonymMatchScore == null)
      jcas.throwFeatMissing("synonymMatchScore", "edu.cmu.lti.qalab.types.CandidateSentence");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_synonymMatchScore, v);}
    
  
 
  /** @generated */
  final Feature casFeat_candAnswerList;
  /** @generated */
  final int     casFeatCode_candAnswerList;
  /** @generated */ 
  public int getCandAnswerList(int addr) {
        if (featOkTst && casFeat_candAnswerList == null)
      jcas.throwFeatMissing("candAnswerList", "edu.cmu.lti.qalab.types.CandidateSentence");
    return ll_cas.ll_getRefValue(addr, casFeatCode_candAnswerList);
  }
  /** @generated */    
  public void setCandAnswerList(int addr, int v) {
        if (featOkTst && casFeat_candAnswerList == null)
      jcas.throwFeatMissing("candAnswerList", "edu.cmu.lti.qalab.types.CandidateSentence");
    ll_cas.ll_setRefValue(addr, casFeatCode_candAnswerList, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CandidateSentence_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_relevanceScore = jcas.getRequiredFeatureDE(casType, "relevanceScore", "uima.cas.Double", featOkTst);
    casFeatCode_relevanceScore  = (null == casFeat_relevanceScore) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_relevanceScore).getCode();

 
    casFeat_sentence = jcas.getRequiredFeatureDE(casType, "sentence", "edu.cmu.lti.qalab.types.Sentence", featOkTst);
    casFeatCode_sentence  = (null == casFeat_sentence) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_sentence).getCode();

 
    casFeat_depMatchScore = jcas.getRequiredFeatureDE(casType, "depMatchScore", "uima.cas.Double", featOkTst);
    casFeatCode_depMatchScore  = (null == casFeat_depMatchScore) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_depMatchScore).getCode();

 
    casFeat_synonymMatchScore = jcas.getRequiredFeatureDE(casType, "synonymMatchScore", "uima.cas.Double", featOkTst);
    casFeatCode_synonymMatchScore  = (null == casFeat_synonymMatchScore) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_synonymMatchScore).getCode();

 
    casFeat_candAnswerList = jcas.getRequiredFeatureDE(casType, "candAnswerList", "uima.cas.FSList", featOkTst);
    casFeatCode_candAnswerList  = (null == casFeat_candAnswerList) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_candAnswerList).getCode();

  }
}



    