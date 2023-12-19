
package expression

abstract class ExpressionException(message: String) extends Exception(message)
final case class EmptyOperandException(message: String) extends ExpressionException(message)
final case class IncompatibleOperandsException(message: String) extends ExpressionException(message)
