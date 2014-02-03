package edu.cmu.lti.oaqa.bagpipes.space.test
import edu.cmu.lti.oaqa.bagpipes.space.DepthExplorer
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._

object experiment {

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
  }                                               //> List((blah,3), (foo,2))
                                                  //| res0: edu.cmu.lti.oaqa.bagpipes.space.test.confTrees{lazy val pathsFromStart
                                                  //| : Stream[(edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Leaf[edu.cmu.lt
                                                  //| i.oaqa.bagpipes.configuration.Descriptors.ExecutableConf], Stream[edu.cmu.lt
                                                  //| i.oaqa.bagpipes.space.ConfigurationSpace.Leaf[edu.cmu.lti.oaqa.bagpipes.conf
                                                  //| iguration.Descriptors.ExecutableConf]])]; lazy val executionPaths: scala.col
                                                  //| lection.immutable.Stream[(edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace
                                                  //| .Leaf[edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf], S
                                                  //| tream[edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace.Leaf[edu.cmu.lti.oa
                                                  //| qa.bagpipes.configuration.Descriptors.ExecutableConf]])]; val list: List[(St
                                                  //| ring, Int)]; def unpack(params: Any*): Seq[Any]} = edu.cmu.lti.oaqa.bagpipes
                                                  //| .space.test.experiment$$anonfun$main$1$$anon$1@553bf378
}