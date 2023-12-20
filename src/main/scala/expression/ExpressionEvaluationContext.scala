
package expression

import table.TableCellPosition

/** context to be passed to expressions when evaluating them */
abstract class ExpressionEvaluationContext {
    def get(pos: TableCellPosition): Expression
}
