package expression

import table.{TableCellPosition, TableCellValue}

final case class ReferenceExpression(pos: TableCellPosition) extends Expression {
    def evaluate(context: ExpressionEvaluationContext): Expression = {
        context.get(pos)
    }
    def referencedPositions: Iterable[TableCellPosition] = Array(pos)
}
