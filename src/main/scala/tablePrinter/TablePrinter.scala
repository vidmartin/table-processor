
package tablePrinter

import table.TableView
import expression.Expression

abstract class TablePrinter[T <: Expression] {
    def printTable(table: TableView[T], destination: StringWriter): Unit
}
