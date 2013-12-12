package edu.cmu.lti.oaqa.bagpipes.db

import java.sql.Timestamp
import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.jdbc.{StaticQuery => Q}
import Database.threadLocalSession
import BagpipesDatabase.{Experiment, DBTrace, Metric}
import javax.sql.rowset.serial.SerialBlob

class SqliteDB(url: String) extends BagpipesDatabase {
  
  val db = Database.forURL(url, driver = "org.sqlite.JDBC")
  
  object Experiments extends Table[Experiment]("experiments") {
    def uuid = column[String]("uuid", O.PrimaryKey)
    def name = column[String]("name", O.NotNull)
    def author = column[String]("author", O.NotNull)
    def config = column[String]("config", O.NotNull)
    def note = column[Option[String]]("note")
    def timestamp = column[Timestamp]("timestamp", O.NotNull)
    // Mapping
    def * = uuid ~ name ~ author ~ config ~ note ~ timestamp <> (Experiment, Experiment.unapply(_))
  }
  
  object Traces extends Table[DBTrace]("traces") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def trace = column[String]("trace", O.NotNull)
    def exp_uuid = column[String]("experiment_uuid")
    def cas_xmi = column[Array[Byte]]("cas_xmi", O.NotNull)
    // Constraints
    def fk = foreignKey("EXP_FK", exp_uuid, Experiments)(_.uuid)
    // Mappings
    def * = id.? ~ trace ~ exp_uuid ~ cas_xmi <> ({t => DBTrace(t._1, t._2, t._3, new SerialBlob(t._4))},
        {(t: DBTrace) => Some((t.id, t.trace, t.expUuid, t.casXmi.getBytes(1, t.casXmi.length().toInt)))})
    def forInsert = trace ~ exp_uuid ~ cas_xmi <> ({t => DBTrace(None, t._1, t._2, new SerialBlob(t._3))},
        {(t: DBTrace) => Some((t.trace, t.expUuid, t.casXmi.getBytes(1, t.casXmi.length().toInt)))})
  }
  
  object Metrics extends Table[Metric]("metrics") {
    def trace_id = column[Int]("trace_id")
    def name = column[String]("name", O.NotNull)
    def target = column[String]("target", O.NotNull)
    def value = column[Double]("value", O.NotNull)
    // Constraints
    def pk = primaryKey("PK", (trace_id, name, target))
    def fk = foreignKey("TRACE_FK", trace_id, Traces)(_.id)
    // Mappings
    def * = trace_id ~ name ~ target ~ value <> (Metric, Metric.unapply(_))
  }
  
  def ddl = Experiments.ddl ++ Traces.ddl ++ Metrics.ddl
  
  /** Do an integrity check on the database.
   *  
   * Check for table integrity using SQLite's built-in integrity check 
   * (PRAGMA integrity_check).  Then, check for the presence of the three primary
   * tables: experiments, traces, and metrics.  If the database is corrupted or 
   * any of the primary tables are absent, return false.
   * 
   * It's advisable to run this before the first database call on an unfamiliar 
   * database.
   * 
   * @return false if the database is corrupted or tables are missing, true otherwise
   */
  def integrityCheck(): Boolean = {
    val tables = Set("experiments", "traces", "metrics")
    db withSession {
    // Do an integrity check
      (Q.queryNA[String]("PRAGMA integrity_check;").first == "ok" &&
          // Check for presence of all the tables
          tables.subsetOf(Q.queryNA[String]("SELECT name FROM sqlite_master WHERE type='table'").elements.to[Set]))
    }
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
  def insertExperiment(exp: Experiment): Unit = {
    db withSession { Experiments.insert(exp) }
  }
  
  /** Insert a Trace into the database. */
  def insertTrace(trc: DBTrace): Unit = {
    db withSession { Traces.forInsert.insert(trc) }
  }
  
  /** Insert a Metric into the database. */
  def insertMetric(mtrc: Metric): Unit = {
    db withSession { Metrics.insert(mtrc) }
  }
  
  /** Retrieve an Experiment from the database.
   * 
   * @param uuid
   * 	String of the unique identifier for the Experiment
   */
  def getExperiment(uuid: String): Option[Experiment] = {
    val q = Query(Experiments).filter(_.uuid === uuid)
    db withSession { 
      try {
        Some(q.first)
      } catch {
        case nseEx: NoSuchElementException => {
          println("No experiment with uuid="+uuid)
          None 
        }
        case e: Exception => None
      }
    }
  }
  
  /** Retrieve a Trace from the database.
   *  
   * @param id
   *  	the trace id
   */
  def getTrace(id: Int): Option[DBTrace] = {
    val q = Query(Traces).filter(_.id === id)
    db withSession {
      try {
      	Some(q.first)
      } catch {
        case nseEx: NoSuchElementException => {
          println("No trace with id="+id)
          None 
        }
        case e: Exception => None
      }
    }
  }
  
  /** Retrieve a Trace from the database.
   * 
   * @param trace
   * 	the trace String (e.g. Component1~Component2~...)
   * @param exp_id
   * 	Unique identifier for the Experiment that this trace belongs to
   */
  def getTrace(trace: String, expUuid: String): Option[DBTrace] = {
    val q = Query(Traces).filter(_.trace === trace).filter(_.exp_uuid === expUuid)
    db withSession { 
      try {
      	Some(q.first)
      } catch {
        case nseEx: NoSuchElementException => {
          println("No trace with trace="+trace+" AND experiment_uuid="+expUuid)
          None 
        }
        case e: Exception => None
      }
    }
  }
  
  /**
   * 
   */
  def getTraces(expUuid: String): List[DBTrace] = {
    val q = Query(Traces).filter(_.exp_uuid === expUuid)
    db withSession { q.elements.to[List] }
  }
  
  /**
   * 
   */
  def getMetric(trace_id: Int, name: String, target: String): Option[Metric] = {
    val q = Query(Metrics).filter(_.trace_id === trace_id).filter(_.name === name).filter(_.target === target)
    db withSession { 
      try {
      	Some(q.first)
      } catch {
        case nseEx: NoSuchElementException => {
          println("No metric with trace_id="+trace_id+" AND name="+name+" AND target="+target)
          None 
        }
        case e: Exception => None
      }
    }
  }
  
  /**
   * 
   */
  def getMetrics(trace_id: Int): List[Metric] = {
    val q = Query(Metrics).filter(_.trace_id === trace_id)
    db withSession { q.elements.to[List] }
  }
}