

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
public class Meeting extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Meeting.class);
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
  protected Meeting() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Meeting(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Meeting(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Meeting(JCas jcas, int begin, int end) {
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
  //* Feature: room

  /** getter for room - gets 
   * @generated */
  public RoomNumber getRoom() {
    if (Meeting_Type.featOkTst && ((Meeting_Type)jcasType).casFeat_room == null)
      jcasType.jcas.throwFeatMissing("room", "org.apache.uima.tutorial.Meeting");
    return (RoomNumber)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Meeting_Type)jcasType).casFeatCode_room)));}
    
  /** setter for room - sets  
   * @generated */
  public void setRoom(RoomNumber v) {
    if (Meeting_Type.featOkTst && ((Meeting_Type)jcasType).casFeat_room == null)
      jcasType.jcas.throwFeatMissing("room", "org.apache.uima.tutorial.Meeting");
    jcasType.ll_cas.ll_setRefValue(addr, ((Meeting_Type)jcasType).casFeatCode_room, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: date

  /** getter for date - gets 
   * @generated */
  public DateAnnot getDate() {
    if (Meeting_Type.featOkTst && ((Meeting_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.tutorial.Meeting");
    return (DateAnnot)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Meeting_Type)jcasType).casFeatCode_date)));}
    
  /** setter for date - sets  
   * @generated */
  public void setDate(DateAnnot v) {
    if (Meeting_Type.featOkTst && ((Meeting_Type)jcasType).casFeat_date == null)
      jcasType.jcas.throwFeatMissing("date", "org.apache.uima.tutorial.Meeting");
    jcasType.ll_cas.ll_setRefValue(addr, ((Meeting_Type)jcasType).casFeatCode_date, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: startTime

  /** getter for startTime - gets 
   * @generated */
  public TimeAnnot getStartTime() {
    if (Meeting_Type.featOkTst && ((Meeting_Type)jcasType).casFeat_startTime == null)
      jcasType.jcas.throwFeatMissing("startTime", "org.apache.uima.tutorial.Meeting");
    return (TimeAnnot)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Meeting_Type)jcasType).casFeatCode_startTime)));}
    
  /** setter for startTime - sets  
   * @generated */
  public void setStartTime(TimeAnnot v) {
    if (Meeting_Type.featOkTst && ((Meeting_Type)jcasType).casFeat_startTime == null)
      jcasType.jcas.throwFeatMissing("startTime", "org.apache.uima.tutorial.Meeting");
    jcasType.ll_cas.ll_setRefValue(addr, ((Meeting_Type)jcasType).casFeatCode_startTime, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: endTime

  /** getter for endTime - gets 
   * @generated */
  public TimeAnnot getEndTime() {
    if (Meeting_Type.featOkTst && ((Meeting_Type)jcasType).casFeat_endTime == null)
      jcasType.jcas.throwFeatMissing("endTime", "org.apache.uima.tutorial.Meeting");
    return (TimeAnnot)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Meeting_Type)jcasType).casFeatCode_endTime)));}
    
  /** setter for endTime - sets  
   * @generated */
  public void setEndTime(TimeAnnot v) {
    if (Meeting_Type.featOkTst && ((Meeting_Type)jcasType).casFeat_endTime == null)
      jcasType.jcas.throwFeatMissing("endTime", "org.apache.uima.tutorial.Meeting");
    jcasType.ll_cas.ll_setRefValue(addr, ((Meeting_Type)jcasType).casFeatCode_endTime, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    