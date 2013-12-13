package edu.cmu.lti.oaqa.bagpipes.controller
import edu.cmu.lti.oaqa.bagpipes.space.Explorer
import edu.cmu.lti.oaqa.bagpipes.executor.Executor
import edu.cmu.lti.oaqa.bagpipes.executor.DataCache
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutableComponent
import edu.cmu.lti.oaqa.bagpipes.executor.ExecutorTypes
import edu.cmu.lti.oaqa.bagpipes.executor._
import edu.cmu.lti.oaqa.bagpipes.space._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ConfigurationDescriptor
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ExecutableConf
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.ComponentDescriptor
import edu.cmu.lti.oaqa.bagpipes.space.test._
object controller extends confTrees {
  object SimpleExecutionController extends ExecutionController(DepthExplorer, SimpleExecutor)
  val controller = SimpleExecutionController      //> controller  : edu.cmu.lti.oaqa.bagpipes.controller.controller.SimpleExecuti
                                                  //| onController.type = edu.cmu.lti.oaqa.bagpipes.controller.controller$SimpleE
                                                  //| xecutionController$@eabce30
  controller.execute(confTree4)()                 //> java.util.NoSuchElementException: key not found: Some(Trace(0,Stream()))
                                                  //| 	at scala.collection.MapLike$class.default(MapLike.scala:228)
                                                  //| 	at scala.collection.AbstractMap.default(Map.scala:58)
                                                  //| 	at scala.collection.MapLike$class.apply(MapLike.scala:141)
                                                  //| 	at scala.collection.AbstractMap.apply(Map.scala:58)
                                                  //| 	at edu.cmu.lti.oaqa.bagpipes.executor.Executor$class.execute(Executor.sc
                                                  //| ala:21)
                                                  //| 	at edu.cmu.lti.oaqa.bagpipes.executor.SimpleExecutor$.execute(SimpleComp
                                                  //| onents.scala:7)
                                                  //| 	at edu.cmu.lti.oaqa.bagpipes.controller.ExecutionController.execStream$1
                                                  //| (ExecutionController.scala:32)
                                                  //| 	at edu.cmu.lti.oaqa.bagpipes.controller.ExecutionController.execute(Exec
                                                  //| utionController.scala:42)
                                                  //| 	at edu.cmu.lti.oaqa.bagpipes.controller.controller$$anonfun$main$1.apply
                                                  //| $mcV$sp(edu.cmu.lti.oaqa.bagpipes.controller.controller.scala:18)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.appl
                                                  //| Output exceeds cutoff limit.
}