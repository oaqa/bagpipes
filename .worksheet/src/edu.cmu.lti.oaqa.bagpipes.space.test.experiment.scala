package edu.cmu.lti.oaqa.bagpipes.space.test
import edu.cmu.lti.oaqa.bagpipes.space.DepthExplorer
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._

object experiment {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(574); val res$0 = 

  new confTrees {
    lazy val pathsFromStart = DepthExplorer.from(confTree3)
    lazy val executionPaths = pathsFromStart.filter {
      case node @ (Leaf(element), _) => true
      case _ => false
    }
    
    executionPaths.take(1)
    
    val list = List(("blah",3),("foo",2))
   def unpack(params: Any *) = for(p <- params) yield p
   println(unpack(list:_*))
    //printf(list :_*)
  };System.out.println("""res0: edu.cmu.lti.oaqa.bagpipes.space.test.confTrees{lazy val pathsFromStart: Stream[(edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Leaf[edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf], Stream[edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Leaf[edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf]])]; lazy val executionPaths: scala.collection.immutable.Stream[(edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Leaf[edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf], Stream[edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Leaf[edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf]])]; val list: List[(String, Int)]; def unpack(params: Any*): Seq[Any]} = """ + $show(res$0))}
}
