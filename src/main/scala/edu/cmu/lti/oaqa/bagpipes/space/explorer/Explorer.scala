package edu.cmu.lti.oaqa.bagpipes.space.explorer
import edu.cmu.lti.oaqa.bagpipes.space.ConfigurationSpace._
import SimpleExplorer._
import edu.cmu.lti.oaqa.bagpipes.scorer.Scorer
import edu.cmu.lti.oaqa.bagpipes.space.Root
import edu.cmu.lti.oaqa.bagpipes.space.TreeWithHistory
import edu.cmu.lti.oaqa.bagpipes.scorer.DefaultScorer

/**
 * Provides some traversal over a configuration space tree in the form of a [[Stream]].
 *
 * Abstract method `from` provides the stream of nodes given by the traversal strategy
 * of this [[Explorer]].
 *
 * @author Avner Maiberg (amaiberg@cs.cmu.edu)
 */

abstract class Explorer[R <: T, T, I](implicit scorer: Scorer[I] = new DefaultScorer[I]()) /* extends HistoryTypes[T]*/ {
  type ExecutableTree = TreeWithHistory[T]
  def from(initial: Stream[TreeWithHistory[T]])(implicit input: I): Stream[TreeWithHistory[T]]
  def fromRoot(root: Root[R, T])(implicit input: I): Stream[TreeWithHistory[T]] = from(Stream(root))
}
 


