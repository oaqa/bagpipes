
/* First created by JCasGen Sun Feb 17 07:02:45 EST 2013 */
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
public class NounPhrase_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (NounPhrase_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = NounPhrase_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new NounPhrase(addr, NounPhrase_Type.this);
  			   NounPhrase_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new NounPhrase(addr, NounPhrase_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = NounPhrase.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.lti.qalab.types.NounPhrase");
 
  /** @generated */
  final Feature casFeat_text;
  /** @generated */
  final int     casFeatCode_text;
  /** @generated */ 
  public String getText(int addr) {
        if (featOkTst && casFeat_text == null)
      jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.NounPhrase");
    return ll_cas.ll_getStringValue(addr, casFeatCode_text);
  }
  /** @generated */    
  public void setText(int addr, String v) {
        if (featOkTst && casFeat_text == null)
      jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.NounPhrase");
    ll_cas.ll_setStringValue(addr, casFeatCode_text, v);}
    
  
 
  /** @generated */
  final Feature casFeat_weight;
  /** @generated */
  final int     casFeatCode_weight;
  /** @generated */ 
  public double getWeight(int addr) {
        if (featOkTst && casFeat_weight == null)
      jcas.throwFeatMissing("weight", "edu.cmu.lti.qalab.types.NounPhrase");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_weight);
  }
  /** @generated */    
  public void setWeight(int addr, double v) {
        if (featOkTst && casFeat_weight == null)
      jcas.throwFeatMissing("weight", "edu.cmu.lti.qalab.types.NounPhrase");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_weight, v);}
    
  
 
  /** @generated */
  final Feature casFeat_synonyms;
  /** @generated */
  final int     casFeatCode_synonyms;
  /** @generated */ 
  public int getSynonyms(int addr) {
        if (featOkTst && casFeat_synonyms == null)
      jcas.throwFeatMissing("synonyms", "edu.cmu.lti.qalab.types.NounPhrase");
    return ll_cas.ll_getRefValue(addr, casFeatCode_synonyms);
  }
  /** @generated */    
  public void setSynonyms(int addr, int v) {
        if (featOkTst && casFeat_synonyms == null)
      jcas.throwFeatMissing("synonyms", "edu.cmu.lti.qalab.types.NounPhrase");
    ll_cas.ll_setRefValue(addr, casFeatCode_synonyms, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public NounPhrase_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_text = jcas.getRequiredFeatureDE(casType, "text", "uima.cas.String", featOkTst);
    casFeatCode_text  = (null == casFeat_text) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_text).getCode();

 
    casFeat_weight = jcas.getRequiredFeatureDE(casType, "weight", "uima.cas.Double", featOkTst);
    casFeatCode_weight  = (null == casFeat_weight) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_weight).getCode();

 
    casFeat_synonyms = jcas.getRequiredFeatureDE(casType, "synonyms", "uima.cas.FSList", featOkTst);
    casFeatCode_synonyms  = (null == casFeat_synonyms) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_synonyms).getCode();

  }
}



    