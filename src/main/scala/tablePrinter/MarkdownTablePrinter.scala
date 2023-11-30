
package tablePrinter

import config.CsvConfig
import expression.ConstantExpression
import expression.Expression
import table.TableView
import stringWriter.StringWriter

class MarkdownTablePrinter[T <: Expression](
    expressionFormatter: ExpressionFormatter[T]
) extends TablePrinter[T] {
    override def printTable(table: TableView[T], destination: StringWriter): Unit = ???
}
