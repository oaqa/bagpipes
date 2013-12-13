

/* First created by JCasGen Wed Feb 20 04:59:42 EST 2013 */
package edu.cmu.lti.oaqa.bagpipes.annotation.temp.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon May 13 14:41:33 EDT 2013
 * XML source: /media/alkesh/Windows7_OS/Users/alkesh/git/qa4mre-cse/qa4mre-base/src/main/resources/TypeSystemDescriptor.xml
 * @generated */
public class Synonym extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Synonym.class);
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
  protected Synonym() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Synonym(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Synonym(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Synonym(JCas jcas, int begin, int end) {
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
  //* Feature: text

  /** getter for text - gets 
   * @generated */
  public String getText() {
    if (Synonym_Type.featOkTst && ((Synonym_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.Synonym");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Synonym_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated */
  public void setText(String v) {
    if (Synonym_Type.featOkTst && ((Synonym_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.Synonym");
    jcasType.ll_cas.ll_setStringValue(addr, ((Synonym_Type)jcasType).casFeatCode_text, v);}    
   
    
  //*--------------*
  //* Feature: source

  /** getter for source - gets 
   * @generated */
  public String getSource() {
    if (Synonym_Type.featOkTst && ((Synonym_Type)jcasType).casFeat_source == null)
      jcasType.jcas.throwFeatMissing("source", "edu.cmu.lti.qalab.types.Synonym");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Synonym_Type)jcasType).casFeatCode_source);}
    
  /** setter for source - sets  
   * @generated */
  public void setSource(String v) {
    if (Synonym_Type.featOkTst && ((Synonym_Type)jcasType).casFeat_source == null)
      jcasType.jcas.throwFeatMissing("source", "edu.cmu.lti.qalab.types.Synonym");
    jcasType.ll_cas.ll_setStringValue(addr, ((Synonym_Type)jcasType).casFeatCode_source, v);}    
   
    
  //*--------------*
  //* Feature: weight

  /** getter for weight - gets 
   * @generated */
  public double getWeight() {
    if (Synonym_Type.featOkTst && ((Synonym_Type)jcasType).casFeat_weight == null)
      jcasType.jcas.throwFeatMissing("weight", "edu.cmu.lti.qalab.types.Synonym");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Synonym_Type)jcasType).casFeatCode_weight);}
    
  /** setter for weight - sets  
   * @generated */
  public void setWeight(double v) {
    if (Synonym_Type.featOkTst && ((Synonym_Type)jcasType).casFeat_weight == null)
      jcasType.jcas.throwFeatMissing("weight", "edu.cmu.lti.qalab.types.Synonym");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Synonym_Type)jcasType).casFeatCode_weight, v);}    
  }

    