
package tablePrinter

import config.CsvConfig
import expression.ConstantExpression
import expression.Expression
import table.TableView
import table.TableCellPosition
import stringWriter.StringWriter
import filters.RowFilter

class CsvTablePrinter[T <: Expression](
    csvConfig: CsvConfig,
    expressionFormatter: ExpressionFormatter[T]
) extends TablePrinter[T] {
    override def printTable(options: TablePrintOptions[T], destination: StringWriter): Unit = {
        // TODO: escaping
        if (options.headerRow) {
            printHeaderRow(options, destination)
        }
        for (i <- Iterable.range(0, options.tableView.lastLocalRow.getOrElse(0) + 1)) {
            if (options.rowFilter.evaluate(i, options.tableView)) {
                printRow(options, destination, i)
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
            j => options.tableView.getLocal(TableCellPosition(row, j)).map(
                cell => expressionFormatter.format(cell.expr)
            ).getOrElse(""),
            if (options.headerColumn) {
                Some(
                    TableCellPosition.getRowName(options.tableView.getGlobalRow(row))
                )
            } else {
                None
            }
        )
    }

    private def printRowWith(table: TableView[T], destination: StringWriter, contentGetter: Int => String, firstCell: Option[String]): Unit = {
        val lastColumn = table.lastLocalColumn.getOrElse(-1)
        destination.writeln(
            Iterable.concat(
                firstCell.toList,
                Iterable.range(0, lastColumn + 1).map(
                    i => contentGetter(i)
                )
            ).mkString(csvConfig.cellSeparator)
        )
    }

}
