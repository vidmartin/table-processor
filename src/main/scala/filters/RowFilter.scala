
package filters

import table.TableView
import expression.Expression
import expression.EmptyExpression

abstract class RowFilter[T >: EmptyExpression.type <: Expression] {
    def evaluate(row: Int, table: TableView[T]): Boolean
}
