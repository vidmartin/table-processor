package expression

import table.{TableCellPosition, TableCellValue}

final case class ReferenceExpression(pos: TableCellPosition) extends Expression {
    def evaluate(context: ExpressionEvaluationContext): ConstantExpression = {
        context.get(pos).evaluate(context)
    }
    def referencedPositions: Iterable[TableCellPosition] = Array(pos)
}
