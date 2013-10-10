

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
public class UimaAcronym extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(UimaAcronym.class);
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
  protected UimaAcronym() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public UimaAcronym(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public UimaAcronym(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public UimaAcronym(JCas jcas, int begin, int end) {
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
  //* Feature: expandedForm

  /** getter for expandedForm - gets 
   * @generated */
  public String getExpandedForm() {
    if (UimaAcronym_Type.featOkTst && ((UimaAcronym_Type)jcasType).casFeat_expandedForm == null)
      jcasType.jcas.throwFeatMissing("expandedForm", "org.apache.uima.tutorial.UimaAcronym");
    return jcasType.ll_cas.ll_getStringValue(addr, ((UimaAcronym_Type)jcasType).casFeatCode_expandedForm);}
    
  /** setter for expandedForm - sets  
   * @generated */
  public void setExpandedForm(String v) {
    if (UimaAcronym_Type.featOkTst && ((UimaAcronym_Type)jcasType).casFeat_expandedForm == null)
      jcasType.jcas.throwFeatMissing("expandedForm", "org.apache.uima.tutorial.UimaAcronym");
    jcasType.ll_cas.ll_setStringValue(addr, ((UimaAcronym_Type)jcasType).casFeatCode_expandedForm, v);}    
  }

    