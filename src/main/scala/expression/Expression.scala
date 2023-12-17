
package expression

import table.TableCellPosition

abstract class Expression {
    def evaluate(context: ExpressionEvaluationContext): ConstantExpression
    def referencedPositions: Iterable[TableCellPosition]
    def isConstant: Boolean = referencedPositions.take(1).size == 0
    def isAtomic: Boolean = false
    def isEmpty: Boolean = false
}
