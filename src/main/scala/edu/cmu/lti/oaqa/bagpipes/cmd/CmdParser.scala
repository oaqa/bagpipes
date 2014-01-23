package edu.cmu.lti.oaqa.bagpipes.cmd

import org.rogach.scallop._
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.DumperOptions
import scala.collection.JavaConverters._
import org.yaml.snakeyaml.DumperOptions
import org.rogach.scallop.ScallopOption
import org.rogach.scallop.exceptions._
import breeze.stats.distributions._
//import com.github.mdr.ascii.layout._
//import com.github.mdr.ascii.graph.Graph
//import com.github.mdr.ascii.layout.coordAssign.Layouter
import edu.cmu.lti.oaqa.bagpipes.run.BagPipesRun
object CmdParser extends App {
  import Parser._
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
  override def main(args: Array[String]): Unit = {
    parse(args)
  }

  def parse(args: Array[String]) = {
    val command = new BPConf(args)
    //val phase = Map()
    //parse commands

    val groupedCmds = command.subcommands.groupBy(isYamlCmd(command, _))
    val yamlCmds = if (groupedCmds.keySet.contains(true)) groupedCmds(true) else Nil
    val otherCmds = if (groupedCmds.keySet.contains(false)) Some(groupedCmds(false).head) else None

    //output yaml

    val descriptor = if (!yamlCmds.isEmpty && !command.quiet.isSupplied) {
      val descriptor = parseYamlCmds(command, yamlCmds)
      val options = new DumperOptions()
      options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
      val yaml = new Yaml(options)
      val out = yaml.dump(descriptor.asJava)
      println(out)
      Some(descriptor)
    } else None

    if (otherCmds != None)
      parseExecCmds(command, otherCmds.get, descriptor)

  }

  //> parse: (args: Array[String])Unit
  /*
  object Conf extends ScallopConf(Seq("tree", "-s")) {
    val tree = new Subcommand("tree") {
      val some = toggle("some")
      val apples = opt[String]("apples")
      val bananas = opt[List[Int]]("bananas")
      codependent(apples, bananas)
    }
  }
  Conf
  */

  object Parser {

    //TODO:
    //figure out deployment (more or less done)
    //Maintain a list of available components when the "create" command is used
    //see if can have Gentoo-style menus for selecting components
    //map components to Integer Ids that can be used instead of the full path
    //infer component path just by looking at name (i.e., NER -> foo.bar.NER)
    //(*) Visualize results using TeX or html
    //(*) execute descriptors, should take a file or directory
    //Handle cmd-line style paths (e.g., edu/cmu/lti/oaqa/bagpipes/component/dummyComponent.java)
    //Visualize space using graphViz (expanded and non-expanded)
    //generate YAML or JSON
    //support other distributions (Chi^2, Poi, Cantor dust...)
    //have a --template-project option that generates all the roomannotator examples
    //(*) implement the "append" toggle...
    // Two approaches:
    // (1) read in file into a map and nest the statement in the selected phase
    // (2) if "append" is selected 
    sealed class BPConf(args: Array[String]) extends ScallopConf(args) {
      version("BagPipes 0.0.1 (c) 2014 Avner Maiberg (amaiberg@cs.cmu.edu)")
      banner("""Usage: bp [OPTION]... |init|pl|create|+ |OPTION|...
           |bp generates, manages, and executes pipeline descriptors using the BagPipes framework.      
           |Options:
           |""".stripMargin)
      footer("Comments, suggestions, or improvements? Visit https://github.com/oaqa/bagpipes")

      val append = toggle("append") //adding nested components
      val pipe = toggle("pipe") // piping in input from other bp command a | b
      val quiet = toggle("quiet")
      case class PipelineCmd(isNew: Boolean = false) extends Subcommand("pl") {
        //temporary workaround for issue #82 on org.rogach.scallop
        override def codependent(c: ScallopOption[_]*) = { dependsOnAny(c(0), List(c(1))); dependsOnAny(c(1), List(c(0))) }
        val start = toggle("new", default = Some(isNew))
        val phase = opt[String]("phase", default = Some("defaultPhase"))
        //val options = opt[Map[String, String]]("options")
        val options = toggle("options", short = 'o')
        val component = opt[String]("component")
        val parameters = props[String]('P')
        //val components = props[String]('C')
        //val multiParameters = ???

        val cross = opt[String]("cross", short = 'C')

        val dist = opt[String]("dist", default = Some("gauss")) //consider making distributions into a subcommand
        val mu = opt[Double]("mu", default = Some(0.5d))
        val sigma = opt[Double]("sigma", default = Some(0.24333))
        val param = opt[String]("param", short = 'X')
        //val crossParamNames = opt[List[String]]("params")
        val numComponents = opt[Int]("numComponents", short = 'N', default = Some(10))
        val begin = trailArg[Double](default = Some(0d), required = false)
        val end = trailArg[Double](default = Some(1d), required = false)

        dependsOnAny(cross, List(phase))
        codependent(cross, param)
        codependent(options, component)
        dependsOnAny(phase, List(options, cross))
        mutuallyExclusive(options, cross)
        //codependent(cross, dist)
        dependsOnAll(dist, param :: component :: mu :: sigma :: begin :: end :: Nil)
      }

      val pipeline = new PipelineCmd

      val create = new Subcommand("create") {
        val className = opt[String]("class", required = true)
        val params = props[String]('P')
      }

      val initial = new Subcommand("init") {
        //override val rootConfig = this
        val author = opt[String]("author", descr = "the author of the descriptor", required = false, default = Some("default"))
        val name = opt[String]("name", descr = "the name of the descriptor", required = false, default = Some("default"))
        val cr = opt[String]("collection-reader", descr = "specifies the collection reader", required = false, default = Some("collection_reader.filesystem-collection-reader"))
        val pipeline = new PipelineCmd(true)
      }

      val exec = new Subcommand("exec") {
        val conf = trailArg[String]("descriptor")
        //BagPipesRun.
      }
      /*TODO: Maybe use the java libs for grpahviz 
      val viz = new Subcommand("viz") {
        val flat = toggle("flat")
      }
*/
      dependsOnAny(append, List(pipeline.options, pipeline.cross))
    }

    def isYamlCmd(command: BPConf, subCmd: ScallopConf): Boolean = {
      subCmd match {
        case (command.initial | command.PipelineCmd(_) | command.create) => true
        case _ => false
      }

    }

    def parseExecCmds(command: BPConf, subcommand: ScallopConf, descriptor: Option[Map[String, Any]] = None) = {
      (subcommand, descriptor) match {
        case (execCmd @ command.exec, None) => BagPipesRun.run(execCmd.conf())
        case (execCmd @ command.exec, Some(desc)) => ???
        case _ => command.printHelp
      }
    }

    def parseYamlCmds(command: BPConf, subcommands: List[ScallopConf]) =
      subcommands.map(subCmd => parseYamlCmd(command, subCmd)).reduce(_ ++ _)

    def parseYamlCmd(command: BPConf, subCmd: ScallopConf): Map[String, Any] =
      subCmd match {

        //Subcommand: init
        case command.initial =>
          val initCmd = command.initial
          val author = initCmd.author()
          val name = initCmd.name()
          val confProps = Map("author" -> author, "name" -> name)
          val collReader = Map("collection-reader" -> Map("inherit" -> initCmd.cr()).asJava)
          val conf = confProps ++ collReader
          Map("configuration" -> conf.asJava)

        //Subcommand: pl
        case pipelineCmd @ command.PipelineCmd(_) =>
          //convenience function for creating components
          def createComponent(name: String, params: Map[String, String]) = Map("inherit" -> name, "params" -> params.asJava).asJava

          val options: List[java.util.Map[String, Object]] =
            if (pipelineCmd.options.isSupplied) // --options
              List(createComponent(pipelineCmd.component(), pipelineCmd.parameters))
            else if (pipelineCmd.cross.isSupplied) { // --cross
              val name = pipelineCmd.cross()
              val parameter = pipelineCmd.param()
              val dist = pipelineCmd.dist
              val mu = pipelineCmd.mu()
              val sigma = pipelineCmd.sigma()
              val n = pipelineCmd.numComponents()
              val begin = pipelineCmd.begin()
              val end = pipelineCmd.end()
              val gauss = new Gaussian(mu, sigma) //for now just assume gaussian distribution, in the future there may be others
              val samples = gauss.sample(n).map(_ * (end - begin))
              val components = for (s <- samples.sortWith(_ < _)) yield {
                createComponent(name, Map(parameter -> s.toString))
              }
              components.toList
            } else List(Map[String, Object]().asJava)

          val phases = if (pipelineCmd.phase.isSupplied) Map("phase" -> pipelineCmd.phase(), "options" -> options.asJava).asJava else List(options.head).asJava

          if (pipelineCmd.start.isSupplied)
            Map("pipeline" -> phases)
          else Map("" -> phases)

        //Subcommand: create
        case createCmd @ command.create =>
          val params: Map[String, String] = createCmd.params
          Map("class" -> createCmd.className(), "params" -> params.asJava)
        //Subcommand: viz
        /*    case vizCmd @ command.viz =>
          implicit def map2Graph(map: Map[String,Any]) : Graph[String] ={
            type V = Set[Any]
            type E = List[Any]
            def map2Graph(v: V = Set(), e: E = Nil) : (V,E)  = {        
              ???
            }
            ???
          }
          val graph = Graph(
            vertices = Set(
              "V1", "V2", "V3", "V4", "V5", "V6", "V7"),
            edges = List(
              "V1" -> "V2",
              "V7" -> "V1",
              "V1" -> "V3",
              "V1" -> "V4",
              "V2" -> "V5",
              "V2" -> "V6"))
          println(graph)
          Map() 
   
   *  
   */ }

  }
}
