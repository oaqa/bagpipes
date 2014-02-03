package edu.cmu.lti.oaqa.bagpipes.db

import java.sql.Timestamp
import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.jdbc.{StaticQuery => Q}
import Database.threadLocalSession
import BagpipesDatabase.{Experiment, Trace, Metric}
import javax.sql.rowset.serial.SerialBlob
import com.typesafe.scalalogging.slf4j.Logging

/** SQLite implementation of BagpipesDatabase.
 *  
 *  @author Collin McCormack
 */
class SqliteDB(url: String) extends BagpipesDatabase with Logging {
  
  val db = Database.forURL(url, driver = "org.sqlite.JDBC")
  
  /** Table to store the details of whole experiments.
   *  
   *  uuid: the unique identifier of an experiment
   *  name: descriptive name for the experiment
   *  author: the person running the experiment
   *  config: the text of the configuration used to launch the experiment
   *  note: free-form text field for notes (optional)
   *  timestamp: when the experiment was launched
   *  
   *  Primary key = uuid
   */
  object Experiments extends Table[Experiment]("experiments") {
    def uuid = column[String]("uuid", O.PrimaryKey)
    def name = column[String]("name", O.NotNull)
    def author = column[String]("author", O.NotNull)
    def config = column[String]("config", O.NotNull)
    def note = column[Option[String]]("note", O.Nullable)
    def timestamp = column[Timestamp]("timestamp", O.NotNull)
    // Mapping
    def * = uuid ~ name ~ author ~ config ~ note ~ timestamp <> (Experiment, Experiment.unapply(_))
  }
  
  /** Table to store the traces of an experiment.
   *  
   *  id: automatically set integer uniquely identifying a trace in the database
   *  trace: the trace string generated from the experiment
   *  exp_uuid: the UUID of the experiment that this trace was generated from,
   *            a foreign key constraint on experiments.uuid
   *  cas_xmi: binary blob of the CAS's XMI at the end of this trace
   *  
   *  Primary key = id
   */
  object Traces extends Table[Trace]("traces") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def trace = column[String]("trace", O.NotNull)
    def exp_uuid = column[String]("experiment_uuid", O.NotNull)
    def cas_xmi = column[Array[Byte]]("cas_xmi", O.NotNull)
    // Constraints
    def fk = foreignKey("EXP_FK", exp_uuid, Experiments)(_.uuid)
    // Mappings
    def * = id ~ trace ~ exp_uuid ~ cas_xmi <> ({t => Trace(t._1, t._2, t._3, new SerialBlob(t._4))},
        {(t: Trace) => Some((t.id, t.trace, t.expUuid, t.casXmi.getBytes(1, t.casXmi.length().toInt)))})
    def forInsert = trace ~ exp_uuid ~ cas_xmi
  }
  
  /** Table to store the metrics for an experiment.
   *  
   *  trace_id: the database ID of the trace from which this metric was generated,
   *            a foreign key constraint on traces.id
   *  name: descriptive name for the metric
   *  target: the target data/scope for the metric
   *  value: the decimal value for the metric
   *  
   *  Primary key = trace_id, name, target
   */
  object Metrics extends Table[Metric]("metrics") {
    def trace_id = column[Int]("trace_id", O.NotNull)
    def name = column[String]("name", O.NotNull)
    def target = column[String]("target", O.NotNull)
    def value = column[Double]("value", O.NotNull)
    // Constraints
    def pk = primaryKey("PK", (trace_id, name, target))
    def fk = foreignKey("TRACE_FK", trace_id, Traces)(_.id)
    // Mappings
    def * = trace_id ~ name ~ target ~ value <> (Metric, Metric.unapply(_))
  }
  
  val ddl = Experiments.ddl ++ Traces.ddl ++ Metrics.ddl
  val allTables = Set("experiments", "traces", "metrics")
  
  /** Do an integrity check on the database.
   *  
   * Check for the presence of all the required tables and their integrity.
   * 
   * It's advisable to run this before the first database call on an unfamiliar 
   * database.
   * 
   * @return false if the database is corrupted or tables are missing, true otherwise
   */
  def integrityCheck(): Boolean = {
    if (checkIntegrity && tablesExist) {
      logger.info("Integrity check PASSED!")
      return true
    } else {
      logger.info("Integrity check FAILED!")
      return false
    }
  }
  
  /** Private helper to perform an integrity check on the database.
   *  
   *  Uses SQLite's built-in integrity check (PRAGMA integrity_check). 
   */
  private def checkIntegrity(): Boolean = db withSession { Q.queryNA[String]("PRAGMA integrity_check;").first == "ok" }
  
  /** Private helper to determine if all of the databases tables are present. */
  private def tablesExist(): Boolean = {
    db withSession { allTables.subsetOf(Q.queryNA[String]("SELECT name FROM sqlite_master WHERE type='table'").elements.to[Set]) }
  }
  
  /** Create the primary tables. */
  def createTables(): Unit = {
    db withSession { ddl.create }
  }
  
  /** Drop the primary tables. 
   *  
   *  WARNING: This will destroy all of the data in the database!
   */
  def dropTables(): Unit = {
    db withSession {
      /* Cannot use ddl.drop here because it will not account for an incomplete 
       * database and will throw an error if a requested table has already been 
       * deleted. Thus, we use raw SQLite statements instead. */
      Q.updateNA("DROP TABLE IF EXISTS metrics").execute
      Q.updateNA("DROP TABLE IF EXISTS traces").execute
      Q.updateNA("DROP TABLE IF EXISTS experiments").execute
    }
  }
  
  /** Insert an Experiment into the database. */
  def insertExperiment(exp: Experiment): Unit = db withSession { Experiments.insert(exp) }
  
  /** Insert a Trace into the database. */
  def insertTrace(trc: Trace): Unit = {
    db withSession { Traces.forInsert.insert(trc.trace, trc.expUuid, trc.casXmi.getBytes(1, trc.casXmi.length().toInt)) }
  }
  
  /** Insert a Metric into the database. */
  def insertMetric(mtrc: Metric): Unit = db withSession { Metrics.insert(mtrc) }
  
  /** Retrieve an Experiment from the database using its UUID.
   * 
   * @param uuid
   * 	String of the unique identifier for the Experiment
   * @return Some(Experiment) if the experiment exists, otherwise None 
   */
  def getExperimentByUuid(uuid: String): Option[Experiment] = {
    val q = Query(Experiments).filter(_.uuid === uuid)
    db withSession { 
      try {
        Some(q.first)
      } catch {
        case nseEx: NoSuchElementException => {
          logger.warn("No experiment with uuid=" + uuid)
          None 
        }
        case e: Exception => {
          logger.error("(getExperimentByUuid) Caught unexpected Exception: " + e.getMessage())
          logger.error("(getExperimentByUuid) Stack trace:" + e.getStackTrace().toString())
          None
        }
      }
    }
  }
  
  /** Retrieve a Trace from the database using its ID.
   *  
   * @param id
   *  	the trace id
   * @return Some(Trace) if the trace exists, otherwise None
   */
  def getTraceById(id: Int): Option[Trace] = {
    val q = Query(Traces).filter(_.id === id)
    db withSession {
      try {
      	Some(q.first)
      } catch {
        case nseEx: NoSuchElementException => {
          logger.warn("(getTrace) No trace with id=" + id)
          None 
        }
        case e: Exception => {
          logger.error("(getTraceById) Caught unexpected exception: " + e.getMessage())
          logger.error("(getTraceById) Stack trace:" + e.getStackTrace().toString())
          None
        }
      }
    }
  }
  
  /** Retrieve a Trace from the database.
   * 
   * @param trace
   * 	the trace String
   * @param exp_id
   * 	Unique identifier for the Experiment that this trace belongs to
   */
  def getTrace(trace: String, expUuid: String): Option[Trace] = {
    val q = Query(Traces).filter(_.trace === trace).filter(_.exp_uuid === expUuid)
    db withSession { 
      try {
      	Some(q.first)
      } catch {
        case nseEx: NoSuchElementException => {
          logger.warn("(getTrace) No trace with trace=" + trace + " AND experiment_uuid=" + expUuid)
          None 
        }
        case e: Exception => {
          logger.error("(getTrace) Caught unexpected exception: " + e.getMessage())
          logger.error("(getTrace) Stack trace:" + e.getStackTrace().toString())
          None
        }
      }
    }
  }
  
  /** Get a list of Traces by their shared Experiment UUID.
   * 
   * @param expUuid
   * 	The Experiment's unique identification string
   * @return A list of Traces with the specified Experiment UUID
   */
  def getTracesByExpId(expUuid: String): List[Trace] = db withSession { Query(Traces).filter(_.exp_uuid === expUuid).list }
  
  /** Retrieve a Metric by its Trace ID, name, and target.
   *  
   * @parameter trace_id
   *  	The ID of the Trace that this Metric was taken from
   * @parameter name
   * 	The name of Metric
   * @parameter target
   * 	The target of the Metric
   * @return Some(Metric) if it exists, otherwise None
   */
  def getMetric(trace_id: Int, name: String, target: String): Option[Metric] = {
    val q = Query(Metrics).filter(_.trace_id === trace_id).filter(_.name === name).filter(_.target === target)
    db withSession { 
      try {
      	Some(q.first)
      } catch {
        case nseEx: NoSuchElementException => {
          logger.warn("No metric with trace_id=" + trace_id + " AND name=" + name + " AND target=" + target)
          None 
        }
        case e: Exception => {
          logger.error("(getMetric) Caught unexpected exception: " + e.getMessage())
          logger.error("(getMetric) Stack trace:" + e.getStackTrace().toString())
          None
        }
      }
    }
  }
  
  /** Retrieve a list of Metrics by the ID of the Trace that owns them.
   *  
   * @parameter trace_id
   * 	The ID of the Trace that this Metric was taken from
   * @return A List of Metrics with the specified Trace ID
   */
  def getMetrics(trace_id: Int): List[Metric] = db withSession { Query(Metrics).filter(_.trace_id === trace_id).list }
}