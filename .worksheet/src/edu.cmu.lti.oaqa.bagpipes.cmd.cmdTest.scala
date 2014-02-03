package edu.cmu.lti.oaqa.bagpipes.cmd
import org.rogach.scallop._
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.DumperOptions
import scala.collection.JavaConverters._
import org.yaml.snakeyaml.DumperOptions
import org.rogach.scallop.ScallopOption
import org.rogach.scallop.exceptions._
import breeze.stats.distributions._
import edu.cmu.lti.oaqa.bagpipes.cmd._


object cmdTest {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(2283); 
  /**
   *
   * bp -n author=mog -p c1=dummy > test.yaml
   * bp -n author=mog c1=dummy
   * bp -n author=mog -p c1="edu.cmu.lti.oaqa.bagpipes.components.dummy"
   * bp -n author=mog -p c1="edu.cmu.lti.oaqa.bagpipes.components.dummy1" c2="edu.cmu.lti.oaqa.bagpipes.components.dummy2"
   * bp --new author=mog --phase c1="edu.cmu.lti.oaqa.bagpipes.components.dummy" c2="edu.cmu.lti.oaqa.bagpipes.components.dummy"
   * bp --new author=mog --phase c1="edu.cmu.lti.oaqa.bagpipes.components.dummy"
   * bp --new author=mog name=test --phase --cross-opts 0 1 c1="edu.cmu.lti.oaqa.bagpipes.components.dummy"
   * bp --new author=mog name=test --phase --cross-opts 0 1 --dist=gauss c1="edu.cmu.lti.oaqa.bagpipes.components.dummy"
   * bp --new author=mog name=test --phase --cross-opts 0 1 --dist=gauss c1="edu.cmu.lti.oaqa.bagpipes.components.dummy" explorer="KBestPath"
   * bp --new author=mog name=test --phase --cross-opts 0 1 d=gauss mu=0.4 sigma=.2482 c1="edu.cmu.lti.oaqa.bagpipes.components.dummy" explorer="KBestPath"
   *
   * OR incremental:
   * //two-phases
   * bp --new author=mog name=test > test.yaml
   * bp --phase c1="edu.cmu.lti.oaqa.bagpipes.components.dummy1" >> test.yaml
   * bp --phase c1="edu.cmu.lti.oaqa.bagpipes.components.dummy2" >> test.yaml
   * //single phase (two components)
   * bp --new author=mog name=test > test.yaml
   * bp --phase c1="edu.cmu.lti.oaqa.bagpipes.components.dummy1" >> test.yaml
   * bp --component c1="edu.cmu.lti.oaqa.bagpipes.components.dummy2" >> test.yaml
   *
   * Useful arg dependency:
   * dependOnAny
   * codependent
   * mutuallyExclusive
   * conflicts
   * etc.
   */
 
  // parse(Array("--help"))
  //parse(Array("--version"))
  //parse(Array("init"))
  //parse(Array("init", "-a", "mog", "-n", "test"))
  //parse(Array("init", "-a", "mog", "-n", "test", "--collection-reader", "dummies.dummyReader"))
  val parser = CmdParser;System.out.println("""parser  : edu.cmu.lti.oaqa.bagpipes.cmd.CmdParser.type = """ + $show(parser ));$skip(31); 

  parser.parse(Array("init"));$skip(44); 
   
  parser.parse(Array("pl", "-n")): Unit;$skip(42); 
  parser.parse(Array("init", "pl", "-n"));$skip(42); 
  parser.parse(Array("init", "pl", "-n"));$skip(130); 
  //with phase
  parser.parse(Array("pl", "-n", "-p", "testPhase", "-oc", "dummies.dummyReader", "-Pp1=foo", "p2=bar", "p3=baz"));$skip(111); 
  //standalone
  parser.parse(Array("pl", "-n", "-oc", "dummies.dummyReader", "-Pp1=foo", "p2=bar", "p3=baz"));$skip(146); 

  //cross-opts with defaults and single parameter
  parser.parse(Array("pl", "-np", "testPhase", "--cross", "dummies.dummyReader", "-X", "foo"));$skip(177); 
  // parse(Array("viz","--flat"))
  //parse(Array("create", "--class", "dummy"))
  parser.parse(Array("create", "--class", "dummy", "-Pp1=32", "p2=43", "p3=aaaa", "p4=12.324"));$skip(41); 
  parser.parse(Array("exec","test-run"))}

}
