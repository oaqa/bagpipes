

/* First created by JCasGen Wed Oct 09 16:52:30 EDT 2013 */
package org.apache.uima.tutorial;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;



/** 
 * Updated by JCasGen Wed Oct 09 16:52:30 EDT 2013
 * XML source: C:/Users/is835d/workspace/bagpipes-new/bagpipes/src/test/resources/types/TutorialTypeSystem.xml
 * @generated */
public class TimeAnnot extends DateTimeAnnot {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TimeAnnot.class);
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
  protected TimeAnnot() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public TimeAnnot(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public TimeAnnot(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public TimeAnnot(JCas jcas, int begin, int end) {
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
     
}

    