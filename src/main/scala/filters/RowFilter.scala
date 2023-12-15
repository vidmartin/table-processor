
package filters

import table.TableView
import expression.Expression

abstract class RowFilter[T <: Expression] {
    def evaluate(row: Int, table: TableView[T]): Boolean
}
