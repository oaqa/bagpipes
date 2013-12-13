

/* First created by JCasGen Sun Feb 03 13:01:15 EST 2013 */
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
public class Sentence extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Sentence.class);
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
  protected Sentence() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Sentence(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Sentence(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Sentence(JCas jcas, int begin, int end) {
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
  //* Feature: id

  /** getter for id - gets 
   * @generated */
  public String getId() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "edu.cmu.lti.qalab.types.Sentence");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Sentence_Type)jcasType).casFeatCode_id);}
    
  /** setter for id - sets  
   * @generated */
  public void setId(String v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_id == null)
      jcasType.jcas.throwFeatMissing("id", "edu.cmu.lti.qalab.types.Sentence");
    jcasType.ll_cas.ll_setStringValue(addr, ((Sentence_Type)jcasType).casFeatCode_id, v);}    
   
    
  //*--------------*
  //* Feature: text

  /** getter for text - gets 
   * @generated */
  public String getText() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.Sentence");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Sentence_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated */
  public void setText(String v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.Sentence");
    jcasType.ll_cas.ll_setStringValue(addr, ((Sentence_Type)jcasType).casFeatCode_text, v);}    
   
    
  //*--------------*
  //* Feature: qualityScore

  /** getter for qualityScore - gets 
   * @generated */
  public double getQualityScore() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_qualityScore == null)
      jcasType.jcas.throwFeatMissing("qualityScore", "edu.cmu.lti.qalab.types.Sentence");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Sentence_Type)jcasType).casFeatCode_qualityScore);}
    
  /** setter for qualityScore - sets  
   * @generated */
  public void setQualityScore(double v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_qualityScore == null)
      jcasType.jcas.throwFeatMissing("qualityScore", "edu.cmu.lti.qalab.types.Sentence");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Sentence_Type)jcasType).casFeatCode_qualityScore, v);}    
   
    
  //*--------------*
  //* Feature: dependencyList

  /** getter for dependencyList - gets 
   * @generated */
  public FSList getDependencyList() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_dependencyList == null)
      jcasType.jcas.throwFeatMissing("dependencyList", "edu.cmu.lti.qalab.types.Sentence");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_dependencyList)));}
    
  /** setter for dependencyList - sets  
   * @generated */
  public void setDependencyList(FSList v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_dependencyList == null)
      jcasType.jcas.throwFeatMissing("dependencyList", "edu.cmu.lti.qalab.types.Sentence");
    jcasType.ll_cas.ll_setRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_dependencyList, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: tokenList

  /** getter for tokenList - gets 
   * @generated */
  public FSList getTokenList() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_tokenList == null)
      jcasType.jcas.throwFeatMissing("tokenList", "edu.cmu.lti.qalab.types.Sentence");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_tokenList)));}
    
  /** setter for tokenList - sets  
   * @generated */
  public void setTokenList(FSList v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_tokenList == null)
      jcasType.jcas.throwFeatMissing("tokenList", "edu.cmu.lti.qalab.types.Sentence");
    jcasType.ll_cas.ll_setRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_tokenList, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: bFilter

  /** getter for bFilter - gets 
   * @generated */
  public boolean getBFilter() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_bFilter == null)
      jcasType.jcas.throwFeatMissing("bFilter", "edu.cmu.lti.qalab.types.Sentence");
    return jcasType.ll_cas.ll_getBooleanValue(addr, ((Sentence_Type)jcasType).casFeatCode_bFilter);}
    
  /** setter for bFilter - sets  
   * @generated */
  public void setBFilter(boolean v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_bFilter == null)
      jcasType.jcas.throwFeatMissing("bFilter", "edu.cmu.lti.qalab.types.Sentence");
    jcasType.ll_cas.ll_setBooleanValue(addr, ((Sentence_Type)jcasType).casFeatCode_bFilter, v);}    
   
    
  //*--------------*
  //* Feature: phraseList

  /** getter for phraseList - gets 
   * @generated */
  public FSList getPhraseList() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_phraseList == null)
      jcasType.jcas.throwFeatMissing("phraseList", "edu.cmu.lti.qalab.types.Sentence");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_phraseList)));}
    
  /** setter for phraseList - sets  
   * @generated */
  public void setPhraseList(FSList v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_phraseList == null)
      jcasType.jcas.throwFeatMissing("phraseList", "edu.cmu.lti.qalab.types.Sentence");
    jcasType.ll_cas.ll_setRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_phraseList, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: nerList

  /** getter for nerList - gets 
   * @generated */
  public FSList getNerList() {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_nerList == null)
      jcasType.jcas.throwFeatMissing("nerList", "edu.cmu.lti.qalab.types.Sentence");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_nerList)));}
    
  /** setter for nerList - sets  
   * @generated */
  public void setNerList(FSList v) {
    if (Sentence_Type.featOkTst && ((Sentence_Type)jcasType).casFeat_nerList == null)
      jcasType.jcas.throwFeatMissing("nerList", "edu.cmu.lti.qalab.types.Sentence");
    jcasType.ll_cas.ll_setRefValue(addr, ((Sentence_Type)jcasType).casFeatCode_nerList, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    