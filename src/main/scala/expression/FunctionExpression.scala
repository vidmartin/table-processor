package expression

import table.TableCellPosition

final case class FunctionExpression(
    func: function.Function,
    params: Array[Expression]
) extends Expression {
    def evaluate(context: ExpressionEvaluationContext): Expression = {
        func.evaluate(context, params)
    }
    def referencedPositions: Iterable[TableCellPosition] = params.flatMap(_.referencedPositions)
}
