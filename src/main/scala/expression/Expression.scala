
package expression

import table.TableCellValue
import table.TableCellPosition

abstract class ExpressionEvaluationContext {
    def get(pos: TableCellPosition): Expression
}

abstract class ConstantExpression extends Expression with FinalExpression {
    override def evaluate(context: ExpressionEvaluationContext): FinalExpression = this
    def getOne: Option[ConstantExpression] = Some(this)
    override def isAtomic: Boolean = true
}

final case object EmptyExpression extends ConstantExpression
final case class IntExpression(value: Int) extends ConstantExpression
final case class FloatExpression(value: Double) extends ConstantExpression
final case class StringExpression(value: String) extends ConstantExpression

final case class ArrayExpression(
    expressions: Array[Expression]
) extends Expression with IterableExpression[Expression] with FinalExpression {
    def referencedPositions: Iterable[TableCellPosition] = {
        expressions.flatMap(expr => expr.referencedPositions)
    }
    def elements(context: ExpressionEvaluationContext): Iterable[Expression] = expressions
    def getOne: Option[ConstantExpression] = None
}

abstract class Expression {
    def evaluate(context: ExpressionEvaluationContext): FinalExpression
    def referencedPositions: Iterable[TableCellPosition]
    def isConstant: Boolean = referencedPositions.take(1).size == 0
    def isAtomic: Boolean = false
}

trait IterableExpression[T <: Expression] extends Expression {
    def elements(context: ExpressionEvaluationContext): Iterable[T]
}

trait FinalExpression extends Expression {
    override def referencedPositions: Iterable[TableCellPosition] = Iterable.empty
    def getOne: Option[ConstantExpression]
}
