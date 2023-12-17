
package evaluation

abstract class EvaluationException(message: String) extends Exception(message)
final case class UnexpectedCycleException[T](message: String, prunedGraph: DirectedGraph[T]) extends EvaluationException(message)
final case class CircularReferenceException(message: String) extends EvaluationException(message)
final case class UnknownNodeException(message: String) extends EvaluationException(message)