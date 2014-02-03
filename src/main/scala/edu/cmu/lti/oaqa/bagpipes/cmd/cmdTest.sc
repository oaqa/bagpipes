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


object cmdTest {
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
  val parser = CmdParser                          //> parser  : edu.cmu.lti.oaqa.bagpipes.cmd.CmdParser.type = edu.cmu.lti.oaqa.b
                                                  //| agpipes.cmd.CmdParser$@c0b76fa

  parser.parse(Array("init"))                     //> configuration:
                                                  //|   author: default
                                                  //|   name: default
                                                  //|   collection-reader:
                                                  //|     inherit: collection_reader.filesystem-collection-reader
                                                  //| 
  
  parser.parse(Array("pl", "-n")): Unit           //> pipeline:
                                                  //| - {}
                                                  //| 
  parser.parse(Array("init", "pl", "-n"))         //> configuration:
                                                  //|   author: default
                                                  //|   name: default
                                                  //|   collection-reader:
                                                  //|     inherit: collection_reader.filesystem-collection-reader
                                                  //| pipeline:
                                                  //| - {}
                                                  //| 
  parser.parse(Array("init", "pl", "-n"))         //> configuration:
                                                  //|   author: default
                                                  //|   name: default
                                                  //|   collection-reader:
                                                  //|     inherit: collection_reader.filesystem-collection-reader
                                                  //| pipeline:
                                                  //| - {}
                                                  //| 
  //with phase
  parser.parse(Array("pl", "-n", "-p", "testPhase", "-oc", "dummies.dummyReader", "-Pp1=foo", "p2=bar", "p3=baz"))
                                                  //> pipeline:
                                                  //|   phase: testPhase
                                                  //|   options:
                                                  //|   - inherit: dummies.dummyReader
                                                  //|     params:
                                                  //|       p1: foo
                                                  //|       p2: bar
                                                  //|       p3: baz
                                                  //| 
  //standalone
  parser.parse(Array("pl", "-n", "-oc", "dummies.dummyReader", "-Pp1=foo", "p2=bar", "p3=baz"))
                                                  //> pipeline:
                                                  //| - inherit: dummies.dummyReader
                                                  //|   params:
                                                  //|     p1: foo
                                                  //|     p2: bar
                                                  //|     p3: baz
                                                  //| 

  //cross-opts with defaults and single parameter
  parser.parse(Array("pl", "-np", "testPhase", "--cross", "dummies.dummyReader", "-X", "foo"))
                                                  //> pipeline:
                                                  //|   phase: testPhase
                                                  //|   options:
                                                  //|   - inherit: dummies.dummyReader
                                                  //|     params:
                                                  //|       foo: '0.31062451653032297'
                                                  //|   - inherit: dummies.dummyReader
                                                  //|     params:
                                                  //|       foo: '0.3418559878726092'
                                                  //|   - inherit: dummies.dummyReader
                                                  //|     params:
                                                  //|       foo: '0.4147289916872558'
                                                  //|   - inherit: dummies.dummyReader
                                                  //|     params:
                                                  //|       foo: '0.4196834959826252'
                                                  //|   - inherit: dummies.dummyReader
                                                  //|     params:
                                                  //|       foo: '0.5490437246656611'
                                                  //|   - inherit: dummies.dummyReader
                                                  //|     params:
                                                  //|       foo: '0.5702428444009585'
                                                  //|   - inherit: dummies.dummyReader
                                                  //|     params:
                                                  //|       foo: '0.5861720364285585'
                                                  //|   - inherit: dummies.dummyReader
                                                  //|     params:
                                                  //|       foo: '0.6005356539093104'
                                                  //|   - inherit: dummies.dummyReader
                                                  //|     params:
                                                  //|       foo: '0.7714751779566793'
                                                  //|   - inherit: dummies.dummyReader
                                                  //|     params:
                                                  //|       foo: '1.0976327288169476'
                                                  //| 
  // parse(Array("viz","--flat"))
  //parse(Array("create", "--class", "dummy"))
  parser.parse(Array("create", "--class", "dummy", "-Pp1=32", "p2=43", "p3=aaaa", "p4=12.324"))
                                                  //> class: dummy
                                                  //| params:
                                                  //|   p1: '32'
                                                  //|   p2: '43'
                                                  //|   p3: aaaa
                                                  //|   p4: '12.324'
                                                  //| 

}