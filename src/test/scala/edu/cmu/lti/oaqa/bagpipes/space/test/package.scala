package edu.cmu.lti.oaqa.bagpipes.space
import edu.cmu.lti.oaqa.bagpipes.CommonTesting._
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors._
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.scorer.Scorer
import edu.cmu.lti.oaqa.bagpipes.executor.Trace
package test {
  trait confTrees extends progConfigs {

    type ExecutableTree = TreeWithHistory[AtomicExecutableConf] with Child
    //tree1 
    val confTree0 = Root[CollectionReaderDescriptor, AtomicExecutableConf](collectionReader)
    //tree2
    val history1 = Stream(collectionReader)
    val confTree1 = Root(collectionReader,
      Stream[ExecutableTree]( //children
        Leaf(roomAnnotator1, history1)))
    //tree3
    val confTree2 = Root(collectionReader,
      Stream[ExecutableTree]( //children
        Leaf(roomAnnotator1, history1), Leaf(roomAnnotator2, history1)))

    val history2 = history1 #::: Stream[AtomicExecutableConf](roomAnnotator1)
    //tree3
    val confTree3 = Root(collectionReader,
      Stream( //children
        Node(roomAnnotator1,
          Stream( // children
            Leaf(simpleDateTimeAnnotator, history2), Leaf(dateTimeAnnotator, history2)), history1)))

    val history3 = history1 #::: Stream[AtomicExecutableConf](roomAnnotator2)
    val confTree4 = Root(collectionReader,
      Stream( //children
        Node(roomAnnotator1,
          Stream( // children
            Leaf(simpleDateTimeAnnotator, history2), Leaf(dateTimeAnnotator, history2)), history1),
        Node(roomAnnotator2,
          Stream( // children
            Leaf(simpleDateTimeAnnotator, history3), Leaf(dateTimeAnnotator, history3)), history1)))

    //tree 5: (cross-opt, expanded)        
    val confTree5 = Root(collectionReader,
      expandedCrossOptAnnotators1.toStream.map((c: AtomicExecutableConf) => Leaf(c, history1)))

    //tree 6: cross-opts (expanded)
    val confTree6 = Root(collectionReader,
      expandedCrossOptAnnotators1.toStream.map((c: AtomicExecutableConf) => Leaf(c, history1)))
      
      
  }

  protected trait scorers extends exploredSpace with confTrees {
    implicit def createTrace(strm: ElementWithHistory): Trace = strm match { case (elem, hist) => Trace(0, hist #::: Stream(elem)) }
    trait TestScorer extends Scorer[Int] {
      val scoreMap: Map[Trace, Double]
      override def score(trace: Trace, result: Option[Int]): Double = if (scoreMap.contains(trace)) scoreMap(trace) else 1
    }

    object TestScorer1 extends TestScorer {
      override val scoreMap = Map[Trace, Double](
        createTrace(roomAnnotator2, history1) -> 2,
        createTrace(roomAnnotator1, history1) -> 3,
        createTrace(dateTimeAnnotator, history2) -> 5)
    }

    object TestScorer2 extends TestScorer {
      override val scoreMap = Map[Trace, Double](
        createTrace(roomAnnotator1, history1) -> 5,
        createTrace(roomAnnotator2, history1) -> 3,
        createTrace(simpleDateTimeAnnotator, history3) -> 5)
    }
  }

  protected abstract sealed trait exploredSpace extends progConfigs with HistoryTypes[AtomicExecutableConf] {
    val exploredConfTree0 = Stream((collectionReader, Stream()))
    val exploredConfTree1 = exploredConfTree0 #::: Stream[ElementWithHistory]((roomAnnotator1, Stream(collectionReader)))
    val sharedConfSubTree1 = Stream[ElementWithHistory]((roomAnnotator2, Stream(collectionReader)))
    val exploredConfTree2 = exploredConfTree1 #::: sharedConfSubTree1
    val sharedHistory1 = Stream(collectionReader, roomAnnotator1)
    val exploredConfTree3 = exploredConfTree1 #::: Stream[ElementWithHistory]((simpleDateTimeAnnotator, sharedHistory1), (dateTimeAnnotator, sharedHistory1))
    val sharedHistory2 = Stream[AtomicExecutableConf](collectionReader, roomAnnotator2)
    val exploredConfSubTree4_1 = Stream[ElementWithHistory]((simpleDateTimeAnnotator, sharedHistory1), (dateTimeAnnotator, sharedHistory1))
    val exploredConfSubTree4_2 = Stream[ElementWithHistory]((simpleDateTimeAnnotator, sharedHistory2), (dateTimeAnnotator, sharedHistory2))
    val exploredConfTree4: Stream[ElementWithHistory]
  }

  trait depthFirst extends exploredSpace {
    override val exploredConfTree4 = exploredConfTree1 #::: exploredConfSubTree4_1 #::: sharedConfSubTree1 #::: exploredConfSubTree4_2
  }

  trait breadthFirst extends exploredSpace {
    override val exploredConfTree4 = exploredConfTree2 #::: exploredConfSubTree4_1 #::: exploredConfSubTree4_2
  }

  trait kBest extends exploredSpace with scorers {
    override val exploredConfTree4 = exploredConfTree1 #::: Stream[ElementWithHistory]((dateTimeAnnotator, sharedHistory1))
    val exploredConfTree4_scorer1 = exploredConfTree1 #::: Stream[ElementWithHistory]((dateTimeAnnotator, sharedHistory1))
    val exploredConfTree4_scorer2 = exploredConfTree0 #::: sharedConfSubTree1 #::: Stream[ElementWithHistory]((simpleDateTimeAnnotator, sharedHistory2))
  }

}