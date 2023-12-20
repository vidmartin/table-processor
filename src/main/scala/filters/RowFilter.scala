
package filters

import table.TableView
import expression.Expression
import expression.EmptyExpression

/** used to filter rows by some logic */
abstract class RowFilter[T >: EmptyExpression.type <: Expression] {
    /** return whether the given row of the given TableView passes this filter */
    def evaluate(row: Int, table: TableView[T]): Boolean
}
