
package expression

import table.TableCellValue
import table.TableCellPosition

abstract class ExpressionEvaluationContext {
    def get(pos: TableCellPosition): Expression
}

abstract class ConstantExpression extends Expression {
    override def evaluate(context: ExpressionEvaluationContext): ConstantExpression = this
    def getOne: Option[ConstantExpression] = Some(this)
    override def isAtomic: Boolean = true
    override def referencedPositions: Iterable[TableCellPosition] = Iterable.empty
}

final case object EmptyExpression extends ConstantExpression
final case class IntExpression(value: Int) extends ConstantExpression
final case class FloatExpression(value: Double) extends ConstantExpression
final case class StringExpression(value: String) extends ConstantExpression

abstract class Expression {
    def evaluate(context: ExpressionEvaluationContext): ConstantExpression
    def referencedPositions: Iterable[TableCellPosition]
    def isConstant: Boolean = referencedPositions.take(1).size == 0
    def isAtomic: Boolean = false
}

trait IterableExpression[T <: Expression] extends Expression {
    def elements(context: ExpressionEvaluationContext): Iterable[T]
}
