
package tablePrinter

import expression.Expression
import table.TableView
import table.{TableCell, TableCellPosition}
import table.TableCellPosition
import expression.StringExpression

class TableViewWithHeaders[T >: StringExpression <: Expression](wrapped: TableView[T]) extends TableView[T] {
    override def get(pos: TableCellPosition): Option[TableCell[T]] = {
        if (pos.row == 0 && pos.column == 0) {
            None
        } else if (pos.row == 0) {
            if (pos.column <= lastColumn.getOrElse(0)) {
                Some(TableCell(StringExpression(TableCellPosition.getColumnName(pos.column - 1))))
            } else {
                None
            }
        } else if (pos.column == 0) {
            if (pos.row <= lastRow.getOrElse(0)) {
                Some(TableCell(StringExpression(TableCellPosition.getRowName(pos.row - 1))))
            } else {
                None
            }
        } else {
            wrapped.get(TableCellPosition(pos.row - 1, pos.column - 1))
        }
    }
    override def lastRow: Option[Int] = wrapped.lastRow.map(_ + 1)
    override def lastColumn: Option[Int] = wrapped.lastColumn.map(_ + 1)
    override def nonEmptyPositions: Iterable[TableCellPosition] = {
        val rowHeaders = Iterable.range(1, lastRow.getOrElse(0) + 1).map(
            TableCellPosition(_, 0)
        )
        val columnHeaders = Iterable.range(1, lastColumn.getOrElse(0) + 1).map(
            TableCellPosition(0, _)
        )
        return Iterable.concat(
            rowHeaders,
            columnHeaders,
            wrapped.nonEmptyPositions.map(pos => TableCellPosition(pos.row + 1, pos.column + 1)),
        )
    }
    override def hasHeaderRow: Boolean = true
    override def hasHeaderColumn: Boolean = true
}
