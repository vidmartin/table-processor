
package tablePrinter

import filters.RowFilter
import expression.Expression
import table.TableView
import expression.EmptyExpression

case class TablePrintOptions[T >: EmptyExpression.type <: Expression](
    tableView: TableView[T],
    rowFilter: RowFilter[T],
    headerColumn: Boolean,
    headerRow: Boolean,
)
