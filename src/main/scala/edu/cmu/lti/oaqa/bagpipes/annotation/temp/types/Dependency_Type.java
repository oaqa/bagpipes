
/* First created by JCasGen Sat Feb 09 22:35:48 EST 2013 */
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
public class Dependency_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Dependency_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Dependency_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Dependency(addr, Dependency_Type.this);
  			   Dependency_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Dependency(addr, Dependency_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Dependency.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.lti.qalab.types.Dependency");
 
  /** @generated */
  final Feature casFeat_governor;
  /** @generated */
  final int     casFeatCode_governor;
  /** @generated */ 
  public int getGovernor(int addr) {
        if (featOkTst && casFeat_governor == null)
      jcas.throwFeatMissing("governor", "edu.cmu.lti.qalab.types.Dependency");
    return ll_cas.ll_getRefValue(addr, casFeatCode_governor);
  }
  /** @generated */    
  public void setGovernor(int addr, int v) {
        if (featOkTst && casFeat_governor == null)
      jcas.throwFeatMissing("governor", "edu.cmu.lti.qalab.types.Dependency");
    ll_cas.ll_setRefValue(addr, casFeatCode_governor, v);}
    
  
 
  /** @generated */
  final Feature casFeat_dependent;
  /** @generated */
  final int     casFeatCode_dependent;
  /** @generated */ 
  public int getDependent(int addr) {
        if (featOkTst && casFeat_dependent == null)
      jcas.throwFeatMissing("dependent", "edu.cmu.lti.qalab.types.Dependency");
    return ll_cas.ll_getRefValue(addr, casFeatCode_dependent);
  }
  /** @generated */    
  public void setDependent(int addr, int v) {
        if (featOkTst && casFeat_dependent == null)
      jcas.throwFeatMissing("dependent", "edu.cmu.lti.qalab.types.Dependency");
    ll_cas.ll_setRefValue(addr, casFeatCode_dependent, v);}
    
  
 
  /** @generated */
  final Feature casFeat_relation;
  /** @generated */
  final int     casFeatCode_relation;
  /** @generated */ 
  public String getRelation(int addr) {
        if (featOkTst && casFeat_relation == null)
      jcas.throwFeatMissing("relation", "edu.cmu.lti.qalab.types.Dependency");
    return ll_cas.ll_getStringValue(addr, casFeatCode_relation);
  }
  /** @generated */    
  public void setRelation(int addr, String v) {
        if (featOkTst && casFeat_relation == null)
      jcas.throwFeatMissing("relation", "edu.cmu.lti.qalab.types.Dependency");
    ll_cas.ll_setStringValue(addr, casFeatCode_relation, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Dependency_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_governor = jcas.getRequiredFeatureDE(casType, "governor", "edu.cmu.lti.qalab.types.Token", featOkTst);
    casFeatCode_governor  = (null == casFeat_governor) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_governor).getCode();

 
    casFeat_dependent = jcas.getRequiredFeatureDE(casType, "dependent", "edu.cmu.lti.qalab.types.Token", featOkTst);
    casFeatCode_dependent  = (null == casFeat_dependent) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_dependent).getCode();

 
    casFeat_relation = jcas.getRequiredFeatureDE(casType, "relation", "uima.cas.String", featOkTst);
    casFeatCode_relation  = (null == casFeat_relation) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_relation).getCode();

  }
}



    