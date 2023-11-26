
package tablePrinter

import config.CsvConfig
import expression.ConstantExpression
import expression.Expression
import table.TableView
import table.TableCellPosition

class CsvTablePrinter[T <: Expression](
    csvConfig: CsvConfig,
    expressionFormatter: ExpressionFormatter[T]
) extends TablePrinter[T] {
    override def printTable(table: TableView[T], destination: StringWriter): Unit = {
        // TODO: escaping
        for (i <- Iterable.range(0, table.lastRow.getOrElse(0) + 1)) {
            printRow(table, destination, i)
        }
    }

    private def printRow(table: TableView[T], destination: StringWriter, row: Int): Unit = {
        val lastColumn = table.lastColumn.getOrElse(0)
        for (j <- Iterable.range(0, lastColumn + 1)) {
            destination.write(table.get(TableCellPosition(row, j)) match {
                case None => ""
                case Some(cell) => expressionFormatter.format(cell.expr)
            })

            destination.write(if (j < lastColumn) {
                csvConfig.cellSeparator
            } else { "\n" })
        }
    }
}
