

/* First created by JCasGen Wed Oct 09 16:52:30 EDT 2013 */
package org.apache.uima.tutorial;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Oct 09 16:52:30 EDT 2013
 * XML source: C:/Users/is835d/workspace/bagpipes-new/bagpipes/src/test/resources/types/TutorialTypeSystem.xml
 * @generated */
public class DateTimeAnnot extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(DateTimeAnnot.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected DateTimeAnnot() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public DateTimeAnnot(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public DateTimeAnnot(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public DateTimeAnnot(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: shortDateString

  /** getter for shortDateString - gets 
   * @generated */
  public String getShortDateString() {
    if (DateTimeAnnot_Type.featOkTst && ((DateTimeAnnot_Type)jcasType).casFeat_shortDateString == null)
      jcasType.jcas.throwFeatMissing("shortDateString", "org.apache.uima.tutorial.DateTimeAnnot");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DateTimeAnnot_Type)jcasType).casFeatCode_shortDateString);}
    
  /** setter for shortDateString - sets  
   * @generated */
  public void setShortDateString(String v) {
    if (DateTimeAnnot_Type.featOkTst && ((DateTimeAnnot_Type)jcasType).casFeat_shortDateString == null)
      jcasType.jcas.throwFeatMissing("shortDateString", "org.apache.uima.tutorial.DateTimeAnnot");
    jcasType.ll_cas.ll_setStringValue(addr, ((DateTimeAnnot_Type)jcasType).casFeatCode_shortDateString, v);}    
  }

    