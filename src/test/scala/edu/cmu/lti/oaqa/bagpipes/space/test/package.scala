package edu.cmu.lti.oaqa.bagpipes.space
import edu.cmu.lti.oaqa.bagpipes.CommonTesting._
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors._
package test {
  trait confTrees extends progConfigs {

    type ExecutableTree = Tree[AtomicExecutableConf]
    //tree1 
    val confTree0 = Root[CollectionReaderDescriptor, AtomicExecutableConf](collectionReader)
    //tree2
    val confTree1 = Root(collectionReader,
      Stream[ExecutableTree]( //children
        Leaf(roomAnnotator1)))
    //tree3
    val confTree2 = Root(collectionReader,
      Stream[ExecutableTree]( //children
        Leaf(roomAnnotator1), Leaf(roomAnnotator2)))

    //tree3
    val confTree3 = Root(collectionReader,
      Stream( //children
        Node(roomAnnotator1,
          Stream[ExecutableTree]( // children
            Leaf(simpleDateTimeAnnotator), Leaf(dateTimeAnnotator)))))

    val confTree4 = Root(collectionReader,
      Stream[ExecutableTree]( //children
        Node(roomAnnotator1,
          Stream[ExecutableTree]( // children
            Leaf(simpleDateTimeAnnotator), Leaf(dateTimeAnnotator))),
        Node(roomAnnotator2,
          Stream[ExecutableTree]( // children
            Leaf(simpleDateTimeAnnotator), Leaf(dateTimeAnnotator)))))

    //tree 5: (cross-opt, expanded)        
    val confTree5 = Root(collectionReader,
      expandedCrossOptAnnotators1.toStream.map((c: AtomicExecutableConf) => Leaf(c)))

    //tree 6: cross-opts (expanded)
    val confTree6 = Root(collectionReader,
      expandedCrossOptAnnotators1.toStream.map((c: AtomicExecutableConf) => Leaf(c)))
  }

  protected abstract sealed trait exploredSpace extends progConfigs with HistoryTypes[AtomicExecutable] {
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

}