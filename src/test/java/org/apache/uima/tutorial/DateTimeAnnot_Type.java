
/* First created by JCasGen Wed Oct 09 16:52:30 EDT 2013 */
package org.apache.uima.tutorial;

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
 * Updated by JCasGen Wed Oct 09 16:52:30 EDT 2013
 * @generated */
public class DateTimeAnnot_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (DateTimeAnnot_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = DateTimeAnnot_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new DateTimeAnnot(addr, DateTimeAnnot_Type.this);
  			   DateTimeAnnot_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new DateTimeAnnot(addr, DateTimeAnnot_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = DateTimeAnnot.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.tutorial.DateTimeAnnot");
 
  /** @generated */
  final Feature casFeat_shortDateString;
  /** @generated */
  final int     casFeatCode_shortDateString;
  /** @generated */ 
  public String getShortDateString(int addr) {
        if (featOkTst && casFeat_shortDateString == null)
      jcas.throwFeatMissing("shortDateString", "org.apache.uima.tutorial.DateTimeAnnot");
    return ll_cas.ll_getStringValue(addr, casFeatCode_shortDateString);
  }
  /** @generated */    
  public void setShortDateString(int addr, String v) {
        if (featOkTst && casFeat_shortDateString == null)
      jcas.throwFeatMissing("shortDateString", "org.apache.uima.tutorial.DateTimeAnnot");
    ll_cas.ll_setStringValue(addr, casFeatCode_shortDateString, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public DateTimeAnnot_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_shortDateString = jcas.getRequiredFeatureDE(casType, "shortDateString", "uima.cas.String", featOkTst);
    casFeatCode_shortDateString  = (null == casFeat_shortDateString) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_shortDateString).getCode();

  }
}



    