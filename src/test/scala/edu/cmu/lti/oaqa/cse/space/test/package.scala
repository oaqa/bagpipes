package edu.cmu.lti.oaqa.cse.space
import edu.cmu.lti.oaqa.cse.CommonTesting._
import edu.cmu.lti.oaqa.cse.scala.space.ConfigurationSpace._
import edu.cmu.lti.oaqa.cse.scala.configuration.Descriptors._
package object test {
  trait confTrees extends progConfigs {
    //tree1 
    val confTree0 = Node(collectionReader, Stream())
    //tree2
    val confTree1 = Node[ExecutableConf](collectionReader,
      Stream( //children
        Leaf(roomAnnotator1)))
    //tree3
    val confTree2 = Node[ExecutableConf](collectionReader,
      Stream( //children
        Leaf(roomAnnotator1), Leaf(roomAnnotator2)))

    //tree3
    val confTree3 = Node[ExecutableConf](collectionReader,
      Stream( //children
        Node(roomAnnotator1,
          Stream( // children
            Leaf(simpleDateTimeAnnotator), Leaf(dateTimeAnnotator)))))

    val confTree4 = Node[ExecutableConf](collectionReader,
      Stream( //children
        Node(roomAnnotator1,
          Stream( // children
            Leaf(simpleDateTimeAnnotator), Leaf(dateTimeAnnotator))),
        Node(roomAnnotator2,
          Stream( // children
            Leaf(simpleDateTimeAnnotator), Leaf(dateTimeAnnotator)))))

    //tree 5: (cross-opt, expanded)        
    val confTree5 = Node[ExecutableConf](collectionReader,
      expandedCrossOptAnnotators1.toStream.map((c: ExecutableConf) => Leaf(c)))

    //tree 6: cross-opts (expanded)
    val confTree6 = Node[ExecutableConf](collectionReader,
      expandedCrossOptAnnotators1.toStream.map((c: ExecutableConf) => Leaf(c)))

  }

}