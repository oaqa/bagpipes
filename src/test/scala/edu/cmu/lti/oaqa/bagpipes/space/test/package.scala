package edu.cmu.lti.oaqa.bagpipes.space
import edu.cmu.lti.oaqa.bagpipes.CommonTesting._
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors._
package test {
  trait confTrees extends progConfigs {
    //tree1 
    val confTree0: Tree[AtomicExecutableConf] = Node(collectionReader)
    //tree2
    val confTree1 = Node[AtomicExecutableConf](collectionReader,
      Stream( //children
        Leaf(roomAnnotator1)))
    //tree3
    val confTree2 = Node[AtomicExecutableConf](collectionReader,
      Stream( //children
        Leaf(roomAnnotator1), Leaf(roomAnnotator2)))

    //tree3
    val confTree3 = Node[AtomicExecutableConf](collectionReader,
      Stream( //children
        Node(roomAnnotator1,
          Stream( // children
            Leaf(simpleDateTimeAnnotator), Leaf(dateTimeAnnotator)))))

    val confTree4 = Node[AtomicExecutableConf](collectionReader,
      Stream( //children
        Node(roomAnnotator1,
          Stream( // children
            Leaf(simpleDateTimeAnnotator), Leaf(dateTimeAnnotator))),
        Node(roomAnnotator2,
          Stream( // children
            Leaf(simpleDateTimeAnnotator), Leaf(dateTimeAnnotator)))))

    //tree 5: (cross-opt, expanded)        
    val confTree5 = Node[AtomicExecutableConf](collectionReader,
      expandedCrossOptAnnotators1.toStream.map((c: AtomicExecutableConf) => Leaf(c)))

    //tree 6: cross-opts (expanded)
    val confTree6 = Node[AtomicExecutableConf](collectionReader,
      expandedCrossOptAnnotators1.toStream.map((c: AtomicExecutableConf) => Leaf(c)))
  }

  protected abstract sealed trait exploredSpace extends progConfigs with HistoryTypes[AtomicExecutableConf] {
    val exploredConfTree0 = Stream[TreeWithHistory]((Leaf(collectionReader), Stream()))
    val exploredConfTree1 = exploredConfTree0 #::: Stream[TreeWithHistory]((Leaf(roomAnnotator1), Stream(Leaf(collectionReader))))
    val sharedConfSubTree1 = Stream[TreeWithHistory]((Leaf(roomAnnotator2), Stream(Leaf(collectionReader))))
    val exploredConfTree2 = exploredConfTree1 #::: sharedConfSubTree1

    val sharedHistory1 = Stream[Leaf[AtomicExecutableConf]](Leaf(collectionReader), Leaf(roomAnnotator1))
    val exploredConfTree3: Stream[TreeWithHistory] = exploredConfTree1 #::: Stream[TreeWithHistory]((Leaf(simpleDateTimeAnnotator), sharedHistory1), (Leaf(dateTimeAnnotator), sharedHistory1))

    val sharedHistory2 = Stream[Leaf[AtomicExecutableConf]](Leaf(collectionReader), Leaf(roomAnnotator2))
    val exploredConfSubTree4_1 = Stream[TreeWithHistory]((Leaf(simpleDateTimeAnnotator), sharedHistory1), (Leaf(dateTimeAnnotator), sharedHistory1))
    val exploredConfSubTree4_2 = Stream[TreeWithHistory]((Leaf(simpleDateTimeAnnotator), sharedHistory2), (Leaf(dateTimeAnnotator), sharedHistory2))
    val exploredConfTree4: Stream[TreeWithHistory]
  }

  trait depthFirst extends exploredSpace {
    override val exploredConfTree4 = exploredConfTree1 #::: exploredConfSubTree4_1 #::: sharedConfSubTree1 #::: exploredConfSubTree4_2
  }

  trait breadthFirst extends exploredSpace {
    override val exploredConfTree4 = exploredConfTree2 #::: exploredConfSubTree4_1 #::: exploredConfSubTree4_2
  }

}