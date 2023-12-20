
package tablePrinter

import table.TableView
import expression.Expression
import stringWriter.StringWriter
import filters.RowFilter
import expression.ConstantExpression
import expression.EmptyExpression

/** knows how to print a table */
abstract class TablePrinter[T >: EmptyExpression.type <: Expression] {
    def printTable(
        options: TablePrintOptions[T],
        destination: StringWriter,
    ): Unit
}
