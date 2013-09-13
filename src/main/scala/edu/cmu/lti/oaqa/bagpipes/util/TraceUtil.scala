package edu.cmu.lti.oaqa.bagpipes.util

/**
 * A collection of utility functions for handling trace strings.
 * @author Collin McCormack
 */
object TraceUtil {
  
  /**
   * Make a new trace, using "~" as the separator.
   * @param base
   * 	The base trace to build from
   * @param suffix
   * 	The name of the new component to add to the trace
   * @return
   * 	The new trace string
   */
  def mkTrace(base: String, suffix: String): String = {
    if (base == null || base == "") suffix
    else base + "~" + suffix
  }
  
  /**
   * Split a trace into its component parts
   * @param trace
   * 	The trace string to split up
   * @return
   * 	String array of the components of the trace
   */
  def splitTrace(trace: String): Array[String] = trace.split("~")
}