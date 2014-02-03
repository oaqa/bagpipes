package edu.cmu.lti.oaqa.bagpipes.space.explorer
import edu.cmu.lti.oaqa.bagpipes.configuration.Descriptors.CollectionReaderDescriptor
import edu.cmu.lti.oaqa.bagpipes.configuration.AbstractDescriptors._
import edu.cmu.lti.oaqa.bagpipes.scorer.Scorer
import edu.cmu.lti.oaqa.bagpipes.space.Leaf
import edu.cmu.lti.oaqa.bagpipes.space.TreeWithChildren
import edu.cmu.lti.oaqa.bagpipes.space.TreeWithHistory
import scala.collection.immutable.Stream.consWrapper

class KBestPathExplorer[I](implicit scorer: Scorer[I]) extends Explorer[CollectionReaderDescriptor, AtomicExecutableConf, I] {
  import BestPath._

  type NodeId = (AtomicExecutableConf, Stream[AtomicExecutableConf])
  trait Path {
    def ++(thatPath: Path): Path
    def **(thatPath: Path): Path
    def getBestTrace(tStream: Stream[TreeWithHistory[AtomicExecutableConf]]): Stream[TreeWithHistory[AtomicExecutableConf]]
  }
  case class EmptyPath extends Path {
    override def ++(thatPath: Path): Path = thatPath
    override def **(thatPath: Path): Path = thatPath
    override def getBestTrace(tStream: Stream[TreeWithHistory[AtomicExecutableConf]]) = Stream()
  }

  //TODO: a lot of room for various code optimizations
  //1. Genericize the Tree type so it does not have to be `AtomicExecutableConf`
  //2. Generalize Path to be an interface for any type that implements ++ and **
  //3. Generalize the explorer to be a transformation of any tree that is provided
  // with a path interface.
  //4. eliminate overlap between ++ and ** in BestPath
  //5. Provide some static methods to eliminate various boilerplate sections
  //6. Basically, implement a "reduce" for a tree
  //7. ...Then think about a "beamed-reduce" for a tree --- one that only looks at a subset thereof
  case class BestPath(current: TreeWithHistory[AtomicExecutableConf], thisWeightMap: Map[NodeId, Double] = Map())(implicit input: I) extends Path {
    val thisKey: NodeId = getId(current)
    val thisWeight: Double = scorer.score(current)
    override def ++(thatPath: Path): Path = thatPath match {
      case BestPath(TreeWithHistory(elem, hist), thatWeightMap) =>
        val thatKey = (elem, hist)
        val thatWeight: Double = thatWeightMap(thatKey)
        val maxWeight = thisWeightMap(thisKey)
        if (thatWeight > maxWeight)
          BestPath(current, thisWeightMap ++ thatWeightMap ++ Map(thatKey -> (thatWeight)))
        else BestPath(current, thisWeightMap ++ thatWeightMap)
      case EmptyPath() => this
    }

    override def **(thatPath: Path): Path = thatPath match {
      case BestPath(TreeWithHistory(elem, hist), thatWeightMap) =>
        val thatKey = (elem, hist)
        val thatWeight: Double = thatWeightMap(thatKey)
        val maxWeight = thisWeightMap(thisKey)
        val newWeight = thatWeight + thisWeight
        val newVal = if (newWeight > maxWeight) newWeight else maxWeight
        BestPath(current, thisWeightMap ++ Map(thisKey -> (newVal)) ++ thatWeightMap)
      case EmptyPath() => this
    }

    def getMax(children: List[TreeWithHistory[AtomicExecutableConf]]) = children.maxBy(x => thisWeightMap((x.getElem, x.getHistory)))
    def getBestTrace(initial: Stream[TreeWithHistory[AtomicExecutableConf]]): Stream[TreeWithHistory[AtomicExecutableConf]] = initial match {
      case Stream() => Stream()
      case (current @ Leaf(_, _)) #:: siblings => Stream(current)
      case (current @ TreeWithChildren(_, children, _)) #:: siblings => {
        val max = Stream(getMax(children.toList))
        current #:: getBestTrace(max)
      }
    }

  }

  object BestPath {
    def getId(tree: TreeWithHistory[AtomicExecutableConf]): (AtomicExecutableConf, Stream[AtomicExecutableConf]) =
      TreeWithHistory.unapply(tree).get
    def apply(tree: TreeWithHistory[AtomicExecutableConf])(implicit input: I): BestPath = {
      val weight = scorer.score(tree)
      BestPath(tree, Map(getId(tree) -> weight))
    }
  }

  override def from(initial: Stream[TreeWithHistory[AtomicExecutableConf]])(implicit input: I): Stream[TreeWithHistory[AtomicExecutableConf]] = {
    def from(initial: Stream[TreeWithHistory[AtomicExecutableConf]]): Path = initial match {
      case Stream() => EmptyPath()
      case (current @ Leaf(_, _)) #:: siblings => BestPath(current) ++ from(siblings)
      case (current @ TreeWithChildren(_, children, _)) #:: siblings =>
        BestPath(current) ** from(children) ++ from(siblings)
    }
    from(initial).getBestTrace(initial)
  }

}

object KBestPathExplorer {
  def apply[I](implicit scorer: Scorer[I]) = new KBestPathExplorer
}
