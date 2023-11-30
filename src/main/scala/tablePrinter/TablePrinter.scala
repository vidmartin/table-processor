
package tablePrinter

import table.TableView
import expression.Expression
import stringWriter.StringWriter

abstract class TablePrinter[T <: Expression] {
    def printTable(table: TableView[T], destination: StringWriter): Unit
}
