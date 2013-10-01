package edu.cmu.lti.oaqa.bagpipes.executor

import scala.collection.immutable.HashMap
import org.apache.uima.jcas.JCas

/**
 * A Map for caching generic executable objects for re-use.
 * 
 * @author Collin McCormack
 */
class ComponentCache[T <: ExecutableComponent[_]] {
  
  var cache = new HashMap[String, T]
  
  /**
   * Add a new component to the internal map with the key id.
   * 
   * @param id
   * 	The key for storing the component
   * @param ae
   * 	The component to cache
   */
  //@see #getId(Class[_ <: AnalysisComponent_ImplBase], List[(String, Any)])
  def +(id: String, comp: T) = cache = cache + (id -> comp)
  
  /**
   * @see #+(String, AnalysisEngine)
   */
  def add(id: String, comp: T) = this + (id, comp)
  
  /**
   * Retrieve the component stored with id
   * 
   * @param id
   * 	The key for retrieving the object
   */
  def get(id: String): T = this.cache(id)
  
  /**
   * Test whether the cache contains a given key.
   * 
   * @param id
   * 	The key to test for inclusion
   * @return
   * 	true if id is present, false otherwise
   */
  def contains(id: String): Boolean = cache.contains(id)
  
  /**
   * Destroy all of the component objects in the cache, releasing their 
   * resources if possible.  (Calling any function for these components after 
   * this will have undocumented effects, DON'T DO IT!)
   */
  def destroy() = {
    cache.values map (x => x.destroy())
  }
}