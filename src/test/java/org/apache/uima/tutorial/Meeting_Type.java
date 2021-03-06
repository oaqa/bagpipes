
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
public class Meeting_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Meeting_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Meeting_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Meeting(addr, Meeting_Type.this);
  			   Meeting_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Meeting(addr, Meeting_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Meeting.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("org.apache.uima.tutorial.Meeting");
 
  /** @generated */
  final Feature casFeat_room;
  /** @generated */
  final int     casFeatCode_room;
  /** @generated */ 
  public int getRoom(int addr) {
        if (featOkTst && casFeat_room == null)
      jcas.throwFeatMissing("room", "org.apache.uima.tutorial.Meeting");
    return ll_cas.ll_getRefValue(addr, casFeatCode_room);
  }
  /** @generated */    
  public void setRoom(int addr, int v) {
        if (featOkTst && casFeat_room == null)
      jcas.throwFeatMissing("room", "org.apache.uima.tutorial.Meeting");
    ll_cas.ll_setRefValue(addr, casFeatCode_room, v);}
    
  
 
  /** @generated */
  final Feature casFeat_date;
  /** @generated */
  final int     casFeatCode_date;
  /** @generated */ 
  public int getDate(int addr) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "org.apache.uima.tutorial.Meeting");
    return ll_cas.ll_getRefValue(addr, casFeatCode_date);
  }
  /** @generated */    
  public void setDate(int addr, int v) {
        if (featOkTst && casFeat_date == null)
      jcas.throwFeatMissing("date", "org.apache.uima.tutorial.Meeting");
    ll_cas.ll_setRefValue(addr, casFeatCode_date, v);}
    
  
 
  /** @generated */
  final Feature casFeat_startTime;
  /** @generated */
  final int     casFeatCode_startTime;
  /** @generated */ 
  public int getStartTime(int addr) {
        if (featOkTst && casFeat_startTime == null)
      jcas.throwFeatMissing("startTime", "org.apache.uima.tutorial.Meeting");
    return ll_cas.ll_getRefValue(addr, casFeatCode_startTime);
  }
  /** @generated */    
  public void setStartTime(int addr, int v) {
        if (featOkTst && casFeat_startTime == null)
      jcas.throwFeatMissing("startTime", "org.apache.uima.tutorial.Meeting");
    ll_cas.ll_setRefValue(addr, casFeatCode_startTime, v);}
    
  
 
  /** @generated */
  final Feature casFeat_endTime;
  /** @generated */
  final int     casFeatCode_endTime;
  /** @generated */ 
  public int getEndTime(int addr) {
        if (featOkTst && casFeat_endTime == null)
      jcas.throwFeatMissing("endTime", "org.apache.uima.tutorial.Meeting");
    return ll_cas.ll_getRefValue(addr, casFeatCode_endTime);
  }
  /** @generated */    
  public void setEndTime(int addr, int v) {
        if (featOkTst && casFeat_endTime == null)
      jcas.throwFeatMissing("endTime", "org.apache.uima.tutorial.Meeting");
    ll_cas.ll_setRefValue(addr, casFeatCode_endTime, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Meeting_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_room = jcas.getRequiredFeatureDE(casType, "room", "org.apache.uima.tutorial.RoomNumber", featOkTst);
    casFeatCode_room  = (null == casFeat_room) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_room).getCode();

 
    casFeat_date = jcas.getRequiredFeatureDE(casType, "date", "org.apache.uima.tutorial.DateAnnot", featOkTst);
    casFeatCode_date  = (null == casFeat_date) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_date).getCode();

 
    casFeat_startTime = jcas.getRequiredFeatureDE(casType, "startTime", "org.apache.uima.tutorial.TimeAnnot", featOkTst);
    casFeatCode_startTime  = (null == casFeat_startTime) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_startTime).getCode();

 
    casFeat_endTime = jcas.getRequiredFeatureDE(casType, "endTime", "org.apache.uima.tutorial.TimeAnnot", featOkTst);
    casFeatCode_endTime  = (null == casFeat_endTime) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_endTime).getCode();

  }
}



    