

/* First created by JCasGen Sun Feb 17 07:02:45 EST 2013 */
package edu.cmu.lti.oaqa.bagpipes.annotation.temp.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon May 13 14:41:33 EDT 2013
 * XML source: /media/alkesh/Windows7_OS/Users/alkesh/git/qa4mre-cse/qa4mre-base/src/main/resources/TypeSystemDescriptor.xml
 * @generated */
public class NounPhrase extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(NounPhrase.class);
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
  protected NounPhrase() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public NounPhrase(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public NounPhrase(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public NounPhrase(JCas jcas, int begin, int end) {
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
    if (NounPhrase_Type.featOkTst && ((NounPhrase_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.NounPhrase");
    return jcasType.ll_cas.ll_getStringValue(addr, ((NounPhrase_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated */
  public void setText(String v) {
    if (NounPhrase_Type.featOkTst && ((NounPhrase_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.NounPhrase");
    jcasType.ll_cas.ll_setStringValue(addr, ((NounPhrase_Type)jcasType).casFeatCode_text, v);}    
   
    
  //*--------------*
  //* Feature: weight

  /** getter for weight - gets 
   * @generated */
  public double getWeight() {
    if (NounPhrase_Type.featOkTst && ((NounPhrase_Type)jcasType).casFeat_weight == null)
      jcasType.jcas.throwFeatMissing("weight", "edu.cmu.lti.qalab.types.NounPhrase");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((NounPhrase_Type)jcasType).casFeatCode_weight);}
    
  /** setter for weight - sets  
   * @generated */
  public void setWeight(double v) {
    if (NounPhrase_Type.featOkTst && ((NounPhrase_Type)jcasType).casFeat_weight == null)
      jcasType.jcas.throwFeatMissing("weight", "edu.cmu.lti.qalab.types.NounPhrase");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((NounPhrase_Type)jcasType).casFeatCode_weight, v);}    
   
    
  //*--------------*
  //* Feature: synonyms

  /** getter for synonyms - gets 
   * @generated */
  public FSList getSynonyms() {
    if (NounPhrase_Type.featOkTst && ((NounPhrase_Type)jcasType).casFeat_synonyms == null)
      jcasType.jcas.throwFeatMissing("synonyms", "edu.cmu.lti.qalab.types.NounPhrase");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((NounPhrase_Type)jcasType).casFeatCode_synonyms)));}
    
  /** setter for synonyms - sets  
   * @generated */
  public void setSynonyms(FSList v) {
    if (NounPhrase_Type.featOkTst && ((NounPhrase_Type)jcasType).casFeat_synonyms == null)
      jcasType.jcas.throwFeatMissing("synonyms", "edu.cmu.lti.qalab.types.NounPhrase");
    jcasType.ll_cas.ll_setRefValue(addr, ((NounPhrase_Type)jcasType).casFeatCode_synonyms, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    