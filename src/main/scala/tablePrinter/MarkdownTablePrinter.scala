
package tablePrinter

import config.CsvConfig
import expression.ConstantExpression
import expression.Expression
import table.TableView
import stringWriter.StringWriter
import table.TableCellPosition
import expression.EmptyExpression

class MarkdownTablePrinter[T <: Expression](
    expressionFormatter: ExpressionFormatter[T]
) extends TablePrinter[T] {
    override def printTable(table: TableView[T], destination: StringWriter): Unit = {
        printHeader(table, destination)
        printRows(table, destination)
    }

    private def printHeader(table: TableView[T], destination: StringWriter): Unit = {
        val lastColumn = table.lastColumn.getOrElse(0)
        if (table.hasHeaderRow) {
            printRow(table, destination, 0)
        } else {
            printRowWith(table, destination, i => "")
        }
        printRowWith(table, destination, i => "---")
    }

    private def printRows(table: TableView[T], destination: StringWriter): Unit = {
        val lastRow = table.lastRow.getOrElse(0)
        for (i <- Iterable.range(getFirstDataRow(table), lastRow + 1)) {
            printRow(table, destination, i)
        }
    }

    private def getFirstDataRow(table: TableView[T]): Int = {
        if (table.hasHeaderRow) { 1 } else { 0 }
    }

    private def printRow(table: TableView[T], destination: StringWriter, row: Int): Unit = {
        printRowWith(
            table,
            destination,
            j => table.get(TableCellPosition(row, j)).map(
                cell => expressionFormatter.format(cell.expr)
            ).getOrElse("")
        )
    }

    private def printRowWith(table: TableView[T], destination: StringWriter, contentGetter: Int => String): Unit = {
        val lastColumn = table.lastColumn.getOrElse(0)
        destination.writeln(
            Iterable.range(0, lastColumn + 1).map(
                i => fixSpaces(contentGetter(i))
            ).mkString("|")
        )
    }

    private def fixSpaces(s: String): String = {
        if (s.forall(c => c.isWhitespace)) {
            "&nbsp;"
        } else {
            s
        }
    }
}