
package tablePrinter

import filters.RowFilter
import expression.Expression
import table.TableView
import expression.EmptyExpression

/** format-independent options for printing tables into textual representations */
case class TablePrintOptions[T >: EmptyExpression.type <: Expression](
    /** the table to be printed */
    tableView: TableView[T],
    /** specify which rows will be printed (an empty AndFilter will mean all rows will be printed) */
    rowFilter: RowFilter[T],
    /** whether a header column should be printed */
    headerColumn: Boolean,
    /** whether a header row should be printed */
    headerRow: Boolean,
)
