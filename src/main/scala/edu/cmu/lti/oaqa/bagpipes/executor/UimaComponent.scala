package edu.cmu.lti.oaqa.bagpipes.executor

import org.uimafit.factory.TypeSystemDescriptionFactory
import org.apache.uima.jcas.JCas

abstract class UimaComponent(cls: Class[_], params: List[_]) extends ExecutableComponent[JCas]{
  override def destroy(): Unit
  override def toString() = UimaComponent.mkId(cls, params)
  def getId(): String = UimaComponent.mkId(cls, params)
}

object UimaComponent {
  // Create a type system description singleton
  val typeSysDesc = getTypeSystem()
  /**
   * Get the type system description using uimaFIT.
   * 
   * Type system descriptors (*.xml) must be in "edu/cmu/lti/oaqa/types/" to be
   * included.
   * 
   * @return TypeSystemDescription for the pipeline
   */
  def getTypeSystem() = {
    System.setProperty("org.uimafit.type.import_pattern", "classpath*:edu/cmu/lti/oaqa/types/**/*.xml")
    TypeSystemDescriptionFactory.createTypeSystemDescription()
  }
  
  /**
   * Make an ID string for a UimaComponent by concatenating the string 
   * representation of the class and parameter list.
   * 
   * @param c
   * 	The Class of the UIMA object
   * @param p
   * 	The List of the UIMA object's parameters
   * @return the ID string
   */
  def mkId(c: Class[_], p: List[_]): String = c.toString() + ":" + p.toString()
  def mkId(s: String, p: List[_]): String = s + ":" + p.toString()
  
  /**
   * Convert a list of parameter tuples into a flat list of Object's.
   * Be sure to unpack the list in the function call with ":_*"!
   * 
   * @param l
   * 	The list of parameters, i.e. 2-tuples
   * @param acc
   * 	An accumulator list for tail-recursive calls
   * @return
   * 	List of Objects that can be used in a uimaFIT analysis engine description factory method
   */
  implicit def flattenParams(l: List[(String, Any)], acc: List[Object]=List()): List[Object] = {
    if (l.isEmpty) acc
    else flattenParams(l.tail, acc :+ l.head._1.asInstanceOf[Object] :+ l.head._2.asInstanceOf[Object])
    // l.head.productIterator.toList
  }
}