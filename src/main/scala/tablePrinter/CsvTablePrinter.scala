
package tablePrinter

import config.CsvConfig
import expression.ConstantExpression
import expression.Expression
import table.TableView

class CsvTablePrinter[T <: Expression](
    csvConfig: CsvConfig,
    expressionFormatter: ExpressionFormatter[T]
) extends TablePrinter[T] {
    override def printTable(table: TableView[T], destination: StringWriter): Unit = ???
}
