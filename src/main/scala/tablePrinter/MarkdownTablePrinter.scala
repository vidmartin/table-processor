
package tablePrinter

import config.CsvConfig
import expression.ConstantExpression
import expression.Expression
import table.TableView
import stringWriter.StringWriter
import table.TableCellPosition
import expression.EmptyExpression
import filters.RowFilter

class MarkdownTablePrinter[T >: ConstantExpression <: Expression](
    expressionFormatter: ExpressionFormatter[T]
) extends TablePrinter[T] {
    override def printTable(options: TablePrintOptions[T], destination: StringWriter): Unit = {
        var printedRows: Int = 0
        if (options.headerRow) {
            printHeaderRow(options, destination)
            printedRows += 1
        }
        
        for (i <- Iterable.range(0, options.tableView.lastLocalRow.getOrElse(0) + 1)) {
            if (printedRows == 1) {
                printSeparator(options, destination)
            }
            if (options.rowFilter.evaluate(i, options.tableView)) {
                printRow(options, destination, i)
                printedRows += 1
            }
        }
    }

    private def printHeaderRow(options: TablePrintOptions[T], destination: StringWriter): Unit = {
        printRowWith(
            options.tableView,
            destination,
            j => TableCellPosition.getColumnName(options.tableView.getGlobalColumn(j)),
            if (options.headerColumn) {
                Some("")
            } else {
                None
            }
        )
    }


    private def printRow(options: TablePrintOptions[T], destination: StringWriter, row: Int): Unit = {
        printRowWith(
            options.tableView,
            destination,
            j => expressionFormatter.format( 
                options.tableView.getLocal(TableCellPosition(row, j)).expr
            ),
            if (options.headerColumn) {
                Some(
                    TableCellPosition.getRowName(options.tableView.getGlobalRow(row))
                )
            } else {
                None
            }
        )
    }

    private def printSeparator(options: TablePrintOptions[T], destination: StringWriter): Unit = {
        printRowWith(
            options.tableView, destination, _ => "---",
            if (options.headerColumn) { Some("---") } else { None }
        )
    }

    private def printRowWith(table: TableView[T], destination: StringWriter, contentGetter: Int => String, firstCell: Option[String]): Unit = {
        val lastColumn = table.lastLocalColumn.getOrElse(-1)
        destination.writeln(
            Iterable.concat(
                firstCell.toList,
                Iterable.range(0, lastColumn + 1).map(contentGetter)
            ).map(fixSpaces).mkString("|")
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
