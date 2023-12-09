
package tablePrinter

import table.TableView
import expression.Expression
import stringWriter.StringWriter
import filters.RowFilter
import expression.ConstantExpression

abstract class TablePrinter[T <: Expression] {
    def printTable(
        table: TableView[T],
        destination: StringWriter,
        rowFilter: RowFilter[T]
    ): Unit
}
