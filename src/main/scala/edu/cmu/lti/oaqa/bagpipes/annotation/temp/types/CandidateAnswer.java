

/* First created by JCasGen Tue Apr 02 22:33:34 EDT 2013 */
package edu.cmu.lti.oaqa.bagpipes.annotation.temp.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon May 13 14:41:33 EDT 2013
 * XML source: /media/alkesh/Windows7_OS/Users/alkesh/git/qa4mre-cse/qa4mre-base/src/main/resources/TypeSystemDescriptor.xml
 * @generated */
public class CandidateAnswer extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(CandidateAnswer.class);
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
  protected CandidateAnswer() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public CandidateAnswer(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public CandidateAnswer(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public CandidateAnswer(JCas jcas, int begin, int end) {
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
  //* Feature: qId

  /** getter for qId - gets 
   * @generated */
  public String getQId() {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_qId == null)
      jcasType.jcas.throwFeatMissing("qId", "edu.cmu.lti.qalab.types.CandidateAnswer");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_qId);}
    
  /** setter for qId - sets  
   * @generated */
  public void setQId(String v) {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_qId == null)
      jcasType.jcas.throwFeatMissing("qId", "edu.cmu.lti.qalab.types.CandidateAnswer");
    jcasType.ll_cas.ll_setStringValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_qId, v);}    
   
    
  //*--------------*
  //* Feature: text

  /** getter for text - gets 
   * @generated */
  public String getText() {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.CandidateAnswer");
    return jcasType.ll_cas.ll_getStringValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated */
  public void setText(String v) {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "edu.cmu.lti.qalab.types.CandidateAnswer");
    jcasType.ll_cas.ll_setStringValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_text, v);}    
   
    
  //*--------------*
  //* Feature: choiceIndex

  /** getter for choiceIndex - gets 
   * @generated */
  public int getChoiceIndex() {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_choiceIndex == null)
      jcasType.jcas.throwFeatMissing("choiceIndex", "edu.cmu.lti.qalab.types.CandidateAnswer");
    return jcasType.ll_cas.ll_getIntValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_choiceIndex);}
    
  /** setter for choiceIndex - sets  
   * @generated */
  public void setChoiceIndex(int v) {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_choiceIndex == null)
      jcasType.jcas.throwFeatMissing("choiceIndex", "edu.cmu.lti.qalab.types.CandidateAnswer");
    jcasType.ll_cas.ll_setIntValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_choiceIndex, v);}    
   
    
  //*--------------*
  //* Feature: PMIScore

  /** getter for PMIScore - gets 
   * @generated */
  public double getPMIScore() {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_PMIScore == null)
      jcasType.jcas.throwFeatMissing("PMIScore", "edu.cmu.lti.qalab.types.CandidateAnswer");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_PMIScore);}
    
  /** setter for PMIScore - sets  
   * @generated */
  public void setPMIScore(double v) {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_PMIScore == null)
      jcasType.jcas.throwFeatMissing("PMIScore", "edu.cmu.lti.qalab.types.CandidateAnswer");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_PMIScore, v);}    
   
    
  //*--------------*
  //* Feature: similarityScore

  /** getter for similarityScore - gets 
   * @generated */
  public double getSimilarityScore() {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_similarityScore == null)
      jcasType.jcas.throwFeatMissing("similarityScore", "edu.cmu.lti.qalab.types.CandidateAnswer");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_similarityScore);}
    
  /** setter for similarityScore - sets  
   * @generated */
  public void setSimilarityScore(double v) {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_similarityScore == null)
      jcasType.jcas.throwFeatMissing("similarityScore", "edu.cmu.lti.qalab.types.CandidateAnswer");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_similarityScore, v);}    
   
    
  //*--------------*
  //* Feature: synonymScore

  /** getter for synonymScore - gets 
   * @generated */
  public double getSynonymScore() {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_synonymScore == null)
      jcasType.jcas.throwFeatMissing("synonymScore", "edu.cmu.lti.qalab.types.CandidateAnswer");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_synonymScore);}
    
  /** setter for synonymScore - sets  
   * @generated */
  public void setSynonymScore(double v) {
    if (CandidateAnswer_Type.featOkTst && ((CandidateAnswer_Type)jcasType).casFeat_synonymScore == null)
      jcasType.jcas.throwFeatMissing("synonymScore", "edu.cmu.lti.qalab.types.CandidateAnswer");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((CandidateAnswer_Type)jcasType).casFeatCode_synonymScore, v);}    
  }

    