package edu.cmu.lti.oaqa.bagpipes.db

import BagpipesDatabase.{Experiment, DBTrace, Metric, getTimestamp}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite
import java.sql.Blob
import scala.util.Random
import javax.sql.rowset.serial.SerialBlob
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SqliteDBTest extends FunSuite with BeforeAndAfter {

  val db: SqliteDB = new SqliteDB("jdbc:sqlite:mem:test")
  var exp: Experiment = _
  var traces: List[DBTrace] = _
  
  /** Return a Blob created from 10 random bytes. 
   *  (Convenience function for testing database blob functions)
   */
  def getRandomBlob(): Blob = {
    val b = Array[Byte](10)
    Random.nextBytes(b)
    new SerialBlob(b)
  }
  
  /**
   * Drop any tables that are leftover from some other test/run, re-create the
   * tables, and create some test data.
   */
  before {
    db.dropTables()
    db.createTables()
    exp = new Experiment("randomID", "experiment name", "Tester", "this: \"is some YAML\"", Some("This is a test entry"), getTimestamp())
    traces = List(new DBTrace(None, "component1", "randomID", getRandomBlob()),
        new DBTrace(None, "component1~componentA", "randomID", getRandomBlob()),
        new DBTrace(None, "component1~componentB", "randomID", getRandomBlob()),
        new DBTrace(None, "component1~componentB~componentZ", "randomID", getRandomBlob()))
  }
  
  /**
   * Drop all the tables.
   */
  after {
    db.dropTables()
  }
  
  test("verify create and drop are working as expected") {
    db.dropTables()
    assert(!db.integrityCheck())
    db.createTables()
    assert(db.integrityCheck())
  }
  
  test("Check Experiment insert and get (verifying results)") {
    db.insertExperiment(exp)
    assert(db.getExperiment("randomID").get === exp)
  }
  
  test("Check Experiment get (no results)") {
    val bad = db.getExperiment("anyID")
    assert(bad.isEmpty)
  }
  
  test("Check Trace insert and get by trace and experiment uuid (verifying results)") {
    // Setup database
    db.insertExperiment(exp)
    traces.map(t => db.insertTrace(t))
    // Values check
    // Trace objects will not test equal because of (1) assignment of id in database, 
    // and (2) SerialBlob objects will not be strictly equal (same object).
    traces.map(t => {
      val s = db.getTrace(t.trace, t.expUuid).get
      assert(s.trace === t.trace)
      assert(s.expUuid === t.expUuid)
      assert(s.casXmi.getBytes(1, s.casXmi.length.toInt) === t.casXmi.getBytes(1, t.casXmi.length.toInt))
      assert(s.id isDefined)
    })
  }
  
  test("Check Trace get by id") {
    // Setup database
    db.insertExperiment(exp)
    traces.map(t => db.insertTrace(t))
    // Get and check
    // Constant '1' assumes that this is the first trace to be inserted, somewhat hacky but should be fine
    val s = db.getTrace(1).get
    val t = traces.head
    assert(s.id.isDefined)
    assert(s.trace === t.trace)
    assert(s.expUuid === t.expUuid)
    assert(s.casXmi.getBytes(1, s.casXmi.length.toInt) === t.casXmi.getBytes(1, t.casXmi.length.toInt))
  }
  
  test("Check Trace get by id (no results)") {
    val bad = db.getTrace(1)
    assert(bad.isEmpty)
  }
  
  test("Check Trace get many by experiment uuid") {
    // Setup database
    db.insertExperiment(exp)
    traces.map(t => db.insertTrace(t))
    // Get and check
    val sl = db.getTraces(exp.uuid)
    assert(sl.length == traces.length)
    sl.map({s => assert(s.expUuid == exp.uuid)})
  }
  
  test("Check Trace get many (no results)") {
    val l = db.getTraces("someID")
    assert(l.isEmpty)
  }
  
  test("Check Metric insert and get by trace id, name, target (verifying results)") {
    // Setup
    db.insertTrace(traces.head)
    val trc = db.getTrace(traces.head.trace, traces.head.expUuid).get
    // Insert
    val metric = Metric(trc.id.get, "MAP", "sentence", 0.85321)
    db.insertMetric(metric)
    // Get and check
    val selMetric = db.getMetric(trc.id.get, "MAP", "sentence")
    assert(metric === selMetric.get)
  }
  
  test("Check Metric get by trace id, name, target (no results)") {
    val bad = db.getMetric(1, "metric", "target")
    assert(bad.isEmpty)
  }
  
  test("Check Metric get many by trace id") {
    // Setup
    db.insertTrace(traces.head)
    val trc = db.getTrace(traces.head.trace, traces.head.expUuid).get
    // Insert
    val metrics = List[Metric](Metric(trc.id.get, "F-measure", "sentence", 0.85321),
        Metric(trc.id.get, "Precision", "paragraph", 0.534),
        Metric(trc.id.get, "Recall", "document", 0.98465))
    metrics.map(m => db.insertMetric(m))
    // Get and check
    val selMetrics = db.getMetrics(trc.id.get)
    assert(selMetrics.toSet.subsetOf(metrics.toSet))
  }
  
  test("Check Metric get many by trace id (no results)") {
    val l = db.getTraces("someID")
    assert(l.isEmpty)
  }
}