
package expression

import table.TableCellPosition

abstract class ExpressionEvaluationContext {
    def get(pos: TableCellPosition): Expression
}
