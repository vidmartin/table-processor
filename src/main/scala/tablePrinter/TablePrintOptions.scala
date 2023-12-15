
package tablePrinter

import filters.RowFilter
import expression.Expression
import table.TableView

case class TablePrintOptions[T <: Expression](
    tableView: TableView[T],
    rowFilter: RowFilter[T],
    headerColumn: Boolean,
    headerRow: Boolean,
)
