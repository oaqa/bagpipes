package edu.cmu.lti.oaqa.bagpipes.executor

import org.apache.uima.cas.CAS
import org.apache.uima.jcas.JCas
import scala.collection.immutable.HashMap

/**
 * A thinly-wrapped Map for intermediate data objects.  The map is keyed on
 * trace strings with generic data objects as the values.
 */
class DataCache[T]() {
  
  var cache = new HashMap[String, T]()
  
  /**
   * Add a new trace-data pair.
   */
  def +(trace: String, item: T) = cache = cache + (trace -> item)
  
  /**
   * @see #+(String, T)
   */
  def add(trace: String, item: T) = this + (trace, item)

  /**
   * Remove the trace-data pair from the cache.
   */
  def -(trace: String) = cache = cache - trace
  
  /**
   * @see #-(String)
   */
  def remove(trace: String) = this - trace
  
  /**
   * Retrieve the data object associated with the trace.
   * @throws NoSuchElementException
   */
  def apply(trace: String): T = cache(trace)
  
  /**
   * @see #apply(String)
   */
  def get(trace: String): T = cache(trace)

  /**
   * Get all the data objects in the container as an Iterable.
   */
  def getContents(): Iterable[T] = cache.values

  /**
   * Get all the traces (keys) as an Iterable.
   */
  def getTraces(): Iterable[String] = cache.keys

  /**
   * Destroy all the values in the cache, releasing their resources if possible.
   */
  def destroy(): Unit = this.getTraces map (key => destroy(cache(key)))
  
  /**
   * Dispose of data objects depending on what their type is.
   */
  private def destroy(item: T): Unit = {
    item match {
      case jcas: JCas => {
        jcas.reset()  // Remove contents
        jcas.release()  // done using it/release to pool
      } case _ => Unit
    }
  }
}