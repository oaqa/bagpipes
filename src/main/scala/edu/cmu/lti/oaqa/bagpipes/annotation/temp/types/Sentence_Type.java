
/* First created by JCasGen Sun Feb 03 13:01:15 EST 2013 */
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
public class Sentence_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Sentence_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Sentence_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Sentence(addr, Sentence_Type.this);
  			   Sentence_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Sentence(addr, Sentence_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Sentence.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.lti.qalab.types.Sentence");
 
  /** @generated */
  final Feature casFeat_id;
  /** @generated */
  final int     casFeatCode_id;
  /** @generated */ 
  public String getId(int addr) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "edu.cmu.lti.qalab.types.Sentence");
    return ll_cas.ll_getStringValue(addr, casFeatCode_id);
  }
  /** @generated */    
  public void setId(int addr, String v) {
        if (featOkTst && casFeat_id == null)
      jcas.throwFeatMissing("id", "edu.cmu.lti.qalab.types.Sentence");
    ll_cas.ll_setStringValue(addr, casFeatCode_id, v);}
    
  
 
  /** @generated */
  final Feature casFeat_text;
  /** @generated */
  final int     casFeatCode_text;
  /** @generated */ 
  public String getText(int addr) {
        if (featOkTst && casFeat_text == null)
      jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.Sentence");
    return ll_cas.ll_getStringValue(addr, casFeatCode_text);
  }
  /** @generated */    
  public void setText(int addr, String v) {
        if (featOkTst && casFeat_text == null)
      jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.Sentence");
    ll_cas.ll_setStringValue(addr, casFeatCode_text, v);}
    
  
 
  /** @generated */
  final Feature casFeat_qualityScore;
  /** @generated */
  final int     casFeatCode_qualityScore;
  /** @generated */ 
  public double getQualityScore(int addr) {
        if (featOkTst && casFeat_qualityScore == null)
      jcas.throwFeatMissing("qualityScore", "edu.cmu.lti.qalab.types.Sentence");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_qualityScore);
  }
  /** @generated */    
  public void setQualityScore(int addr, double v) {
        if (featOkTst && casFeat_qualityScore == null)
      jcas.throwFeatMissing("qualityScore", "edu.cmu.lti.qalab.types.Sentence");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_qualityScore, v);}
    
  
 
  /** @generated */
  final Feature casFeat_dependencyList;
  /** @generated */
  final int     casFeatCode_dependencyList;
  /** @generated */ 
  public int getDependencyList(int addr) {
        if (featOkTst && casFeat_dependencyList == null)
      jcas.throwFeatMissing("dependencyList", "edu.cmu.lti.qalab.types.Sentence");
    return ll_cas.ll_getRefValue(addr, casFeatCode_dependencyList);
  }
  /** @generated */    
  public void setDependencyList(int addr, int v) {
        if (featOkTst && casFeat_dependencyList == null)
      jcas.throwFeatMissing("dependencyList", "edu.cmu.lti.qalab.types.Sentence");
    ll_cas.ll_setRefValue(addr, casFeatCode_dependencyList, v);}
    
  
 
  /** @generated */
  final Feature casFeat_tokenList;
  /** @generated */
  final int     casFeatCode_tokenList;
  /** @generated */ 
  public int getTokenList(int addr) {
        if (featOkTst && casFeat_tokenList == null)
      jcas.throwFeatMissing("tokenList", "edu.cmu.lti.qalab.types.Sentence");
    return ll_cas.ll_getRefValue(addr, casFeatCode_tokenList);
  }
  /** @generated */    
  public void setTokenList(int addr, int v) {
        if (featOkTst && casFeat_tokenList == null)
      jcas.throwFeatMissing("tokenList", "edu.cmu.lti.qalab.types.Sentence");
    ll_cas.ll_setRefValue(addr, casFeatCode_tokenList, v);}
    
  
 
  /** @generated */
  final Feature casFeat_bFilter;
  /** @generated */
  final int     casFeatCode_bFilter;
  /** @generated */ 
  public boolean getBFilter(int addr) {
        if (featOkTst && casFeat_bFilter == null)
      jcas.throwFeatMissing("bFilter", "edu.cmu.lti.qalab.types.Sentence");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_bFilter);
  }
  /** @generated */    
  public void setBFilter(int addr, boolean v) {
        if (featOkTst && casFeat_bFilter == null)
      jcas.throwFeatMissing("bFilter", "edu.cmu.lti.qalab.types.Sentence");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_bFilter, v);}
    
  
 
  /** @generated */
  final Feature casFeat_phraseList;
  /** @generated */
  final int     casFeatCode_phraseList;
  /** @generated */ 
  public int getPhraseList(int addr) {
        if (featOkTst && casFeat_phraseList == null)
      jcas.throwFeatMissing("phraseList", "edu.cmu.lti.qalab.types.Sentence");
    return ll_cas.ll_getRefValue(addr, casFeatCode_phraseList);
  }
  /** @generated */    
  public void setPhraseList(int addr, int v) {
        if (featOkTst && casFeat_phraseList == null)
      jcas.throwFeatMissing("phraseList", "edu.cmu.lti.qalab.types.Sentence");
    ll_cas.ll_setRefValue(addr, casFeatCode_phraseList, v);}
    
  
 
  /** @generated */
  final Feature casFeat_nerList;
  /** @generated */
  final int     casFeatCode_nerList;
  /** @generated */ 
  public int getNerList(int addr) {
        if (featOkTst && casFeat_nerList == null)
      jcas.throwFeatMissing("nerList", "edu.cmu.lti.qalab.types.Sentence");
    return ll_cas.ll_getRefValue(addr, casFeatCode_nerList);
  }
  /** @generated */    
  public void setNerList(int addr, int v) {
        if (featOkTst && casFeat_nerList == null)
      jcas.throwFeatMissing("nerList", "edu.cmu.lti.qalab.types.Sentence");
    ll_cas.ll_setRefValue(addr, casFeatCode_nerList, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Sentence_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_id = jcas.getRequiredFeatureDE(casType, "id", "uima.cas.String", featOkTst);
    casFeatCode_id  = (null == casFeat_id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_id).getCode();

 
    casFeat_text = jcas.getRequiredFeatureDE(casType, "text", "uima.cas.String", featOkTst);
    casFeatCode_text  = (null == casFeat_text) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_text).getCode();

 
    casFeat_qualityScore = jcas.getRequiredFeatureDE(casType, "qualityScore", "uima.cas.Double", featOkTst);
    casFeatCode_qualityScore  = (null == casFeat_qualityScore) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_qualityScore).getCode();

 
    casFeat_dependencyList = jcas.getRequiredFeatureDE(casType, "dependencyList", "uima.cas.FSList", featOkTst);
    casFeatCode_dependencyList  = (null == casFeat_dependencyList) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_dependencyList).getCode();

 
    casFeat_tokenList = jcas.getRequiredFeatureDE(casType, "tokenList", "uima.cas.FSList", featOkTst);
    casFeatCode_tokenList  = (null == casFeat_tokenList) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_tokenList).getCode();

 
    casFeat_bFilter = jcas.getRequiredFeatureDE(casType, "bFilter", "uima.cas.Boolean", featOkTst);
    casFeatCode_bFilter  = (null == casFeat_bFilter) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_bFilter).getCode();

 
    casFeat_phraseList = jcas.getRequiredFeatureDE(casType, "phraseList", "uima.cas.FSList", featOkTst);
    casFeatCode_phraseList  = (null == casFeat_phraseList) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_phraseList).getCode();

 
    casFeat_nerList = jcas.getRequiredFeatureDE(casType, "nerList", "uima.cas.FSList", featOkTst);
    casFeatCode_nerList  = (null == casFeat_nerList) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_nerList).getCode();

  }
}



    