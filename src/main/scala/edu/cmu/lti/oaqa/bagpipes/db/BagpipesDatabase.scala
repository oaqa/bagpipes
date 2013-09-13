package edu.cmu.lti.oaqa.bagpipes.db

import java.sql.Blob
import java.sql.Timestamp
import java.util.{GregorianCalendar => GregCal}

trait BagpipesDatabase {
  import BagpipesDatabase._
  
  def integrityCheck(): Boolean
  def createTables(): Unit
  def dropTables(): Unit
  def insertExperiment(exp: Experiment): Unit
  def insertTrace(trc: Trace): Unit
  def insertMetric(mtrc: Metric): Unit
  def getExperiment(expUuid: String): Option[Experiment]
  def getTrace(id: Int): Option[Trace]
  def getTrace(trace: String, exp_id: String): Option[Trace]
  def getTraces(expUuid: String): List[Trace]
  def getMetric(trace_id: Int, name: String, target: String): Option[Metric]
  def getMetrics(trace_id: Int): List[Metric]
}

object BagpipesDatabase {
  case class Experiment(uuid: String, name: String, author: String, config: String, note: Option[String], timestamp: java.sql.Timestamp)
  case class Trace(id: Option[Int], trace: String, expUuid: String, casXmi: Blob)
  case class Metric(trace_id: Int, name: String, target: String, value: Double)
  
  /** Get a Timestamp object for the current time. */
  def getTimestamp() = new Timestamp(new GregCal().getTimeInMillis())
}