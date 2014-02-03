package edu.cmu.lti.oaqa.bagpipes.db

import java.sql.Blob
import java.sql.Timestamp
import java.util.{GregorianCalendar => GregCal}

/** Interface to the standard database for Bagpipes.
 * 
 * @author Collin McCormack
 */
trait BagpipesDatabase {
  import BagpipesDatabase._
  
  def integrityCheck(): Boolean
  def createTables(): Unit
  def dropTables(): Unit
  def insertExperiment(exp: Experiment): Unit
  def insertTrace(trc: Trace): Unit
  def insertMetric(mtrc: Metric): Unit
  def getExperimentByUuid(expUuid: String): Option[Experiment]
  def getTraceById(id: Int): Option[Trace]
  def getTrace(trace: String, exp_id: String): Option[Trace]
  def getTracesByExpId(expUuid: String): List[Trace]
  def getMetric(trace_id: Int, name: String, target: String): Option[Metric]
  def getMetrics(trace_id: Int): List[Metric]
}

object BagpipesDatabase {
  /** Experiment mapped to database table.
   *  
   *  @param uuid
   *  	Unique identifer for the experiment
   *  @param name
   *  	Descriptive name for the experiment
   *  @param author
   *  	Individual running the experiment
   *  @param config
   *  	the text of the configuration used to launch the experiment
   *  @param note
   *  	free-form text field for notes (optional)
   *  @param timestamp
   *  	when the experiment was launched
   */
  case class Experiment(uuid: String, name: String, author: String, config: String, note: Option[String], timestamp: java.sql.Timestamp)
  /** Trace mapped to database table.
   * 
   *  @param id
   *  	database ID
   *  @param trace
   *  	the trace string generated from the experiment
   *  @param expUuid
   *  	the UUID of the experiment that this trace was generated from
   *  @param casXmi
   *  	binary blob of the CAS's XMI at the end of this trace
   */
  case class Trace(id: Int, trace: String, expUuid: String, casXmi: Blob)
  /** Metric mapped to database table.
   *  
   *  @param trace_id
   *  	the database ID of the trace from which this metric was generated
   *  @param name
   *  	descriptive name for the metric
   *  @param target
   *  	the target data/scope for the metric
   *  @param value
   *  	the decimal value for the metric
   */
  case class Metric(trace_id: Int, name: String, target: String, value: Double)
  
  /** Get a Timestamp object for the current time. */
  def getTimestamp() = new Timestamp(new GregCal().getTimeInMillis())
}