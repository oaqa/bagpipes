package edu.cmu.lti.oaqa.bagpipes.db

import BagpipesDatabase.{Experiment, Trace, Metric, getTimestamp}
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
  // Test data
  val experiments = List(new Experiment("01234", "experiment one", "Tester", "this: \"is some YAML\"", Some("This is a test entry"), getTimestamp()),
        new Experiment("56789", "experiment two", "Tester", "other: \"YAML-ese\"", Some("Second test entry"), getTimestamp()))
  val traces1 = List(new Trace(0, "component1", "01234", getRandomBlob()),
        new Trace(0, "component1~componentA", "01234", getRandomBlob()),
        new Trace(0, "component1~componentB", "01234", getRandomBlob()),
        new Trace(0, "component1~componentB~componentZ", "01234", getRandomBlob()))
  val traces2 = List(new Trace(0, "compZ~compX~Writer", "56789", getRandomBlob()),
        new Trace(0, "compZ~compY~Writer", "56789", getRandomBlob()))
  
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
  
  test("Experiment: insert, get") {
    db.insertExperiment(experiments.head)
    assert(db.getExperimentByUuid(experiments.head.uuid).get === experiments.head)
  }
  
  test("Check Experiment get (no results)") {
    assert(db.getExperimentByUuid("anyID").isEmpty)
  }
  
  test("Trace: insert, get by trace ID and experiment UUID") {
    // Setup
    db.insertExperiment(experiments.head)
    traces1.map(t => db.insertTrace(t))

    // Trace objects will not test equal because of (1) assignment of id in database, 
    // and (2) SerialBlob objects will not be strictly equal (same object).
    traces1.map(t => {
      val result = db.getTrace(t.trace, t.expUuid).get
      assert(result.id > 0) // Ensure the ID gets set
      assert(result.trace === t.trace)
      assert(result.expUuid === t.expUuid)
      assert(result.casXmi.getBytes(1, result.casXmi.length.toInt) === t.casXmi.getBytes(1, t.casXmi.length.toInt))
    })
  }
  
  test("Trace: insert, get list by experiment UUID") {
    // Setup
    experiments.map(exp => db.insertExperiment(exp))
    (traces1 ++ traces2).map(t => db.insertTrace(t))

    val result = db.getTracesByExpId(experiments(1).uuid)
    assert(result.length === traces2.length)
    // A somewhat redundant library check, but it ensures that the right objects are in the result set...
    assert(result.filter(_.expUuid == "56789").length === 2)
    // Negative Result Check
    db.getTracesByExpId("someID").isEmpty
  }
  
  test("Trace: get by id") {
    // Setup database
    db.insertExperiment(experiments.head)
    traces1.map(t => db.insertTrace(t))
    // Negative result
    assert(db.getTraceById(0).isEmpty)
    assert(db.getTraceById(10).isEmpty)

    // Constant '1' assumes that this is the first trace to be inserted, somewhat hacky but should be fine
    val result = db.getTraceById(1).get
    val t = traces1.head
    assert(result.trace === t.trace)
    assert(result.expUuid === t.expUuid)
    assert(result.casXmi.getBytes(1, result.casXmi.length.toInt) === t.casXmi.getBytes(1, t.casXmi.length.toInt))
  }
  
  test("Metric: insert, get by trace id, name, target") {
    // Setup
    db.insertExperiment(experiments.head)
    db.insertTrace(traces1.head)
    val trc = db.getTrace(traces1.head.trace, traces1.head.expUuid).get

    val metric = Metric(trc.id, "MAP", "sentence", 0.85321)
    db.insertMetric(metric)
    
    assert(metric === db.getMetric(trc.id, "MAP", "sentence").get)
    assert(db.getMetric(trc.id, "F1", "keyword").isEmpty)
  }
  
  test("Metric: get list by trace id") {
    // Setup
    db.insertExperiment(experiments.head)
    db.insertTrace(traces1.head)
    val trc = db.getTrace(traces1.head.trace, traces1.head.expUuid).get
    // Insert
    val metrics = List[Metric](Metric(trc.id, "F-measure", "sentence", 0.85321),
        Metric(trc.id, "Precision", "paragraph", 0.534),
        Metric(trc.id, "Recall", "document", 0.98465))
    metrics.map(m => db.insertMetric(m))
    
    assert(db.getMetrics(trc.id).toSet.subsetOf(metrics.toSet))
    assert(db.getMetrics(0).length == 0)
  }
}