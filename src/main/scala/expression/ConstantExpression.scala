package expression

import table.TableCellPosition

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
