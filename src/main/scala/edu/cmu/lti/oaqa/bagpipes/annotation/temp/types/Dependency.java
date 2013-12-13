

/* First created by JCasGen Sat Feb 09 22:35:48 EST 2013 */
package edu.cmu.lti.oaqa.bagpipes.annotation.temp.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon May 13 14:41:33 EDT 2013
 * XML source: /media/alkesh/Windows7_OS/Users/alkesh/git/qa4mre-cse/qa4mre-base/src/main/resources/TypeSystemDescriptor.xml
 * @generated */
public class Dependency extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Dependency.class);
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
  protected Dependency() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Dependency(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Dependency(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Dependency(JCas jcas, int begin, int end) {
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
  //* Feature: governor

  /** getter for governor - gets 
   * @generated */
  public Token getGovernor() {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_governor == null)
      jcasType.jcas.throwFeatMissing("governor", "edu.cmu.lti.qalab.types.Dependency");
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Dependency_Type)jcasType).casFeatCode_governor)));}
    
  /** setter for governor - sets  
   * @generated */
  public void setGovernor(Token v) {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_governor == null)
      jcasType.jcas.throwFeatMissing("governor", "edu.cmu.lti.qalab.types.Dependency");
    jcasType.ll_cas.ll_setRefValue(addr, ((Dependency_Type)jcasType).casFeatCode_governor, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: dependent

  /** getter for dependent - gets 
   * @generated */
  public Token getDependent() {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_dependent == null)
      jcasType.jcas.throwFeatMissing("dependent", "edu.cmu.lti.qalab.types.Dependency");
    return (Token)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Dependency_Type)jcasType).casFeatCode_dependent)));}
    
  /** setter for dependent - sets  
   * @generated */
  public void setDependent(Token v) {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_dependent == null)
      jcasType.jcas.throwFeatMissing("dependent", "edu.cmu.lti.qalab.types.Dependency");
    jcasType.ll_cas.ll_setRefValue(addr, ((Dependency_Type)jcasType).casFeatCode_dependent, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: relation

  /** getter for relation - gets 
   * @generated */
  public String getRelation() {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_relation == null)
      jcasType.jcas.throwFeatMissing("relation", "edu.cmu.lti.qalab.types.Dependency");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Dependency_Type)jcasType).casFeatCode_relation);}
    
  /** setter for relation - sets  
   * @generated */
  public void setRelation(String v) {
    if (Dependency_Type.featOkTst && ((Dependency_Type)jcasType).casFeat_relation == null)
      jcasType.jcas.throwFeatMissing("relation", "edu.cmu.lti.qalab.types.Dependency");
    jcasType.ll_cas.ll_setStringValue(addr, ((Dependency_Type)jcasType).casFeatCode_relation, v);}    
  }

    