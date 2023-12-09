
package table

import expression.Expression

class OffsetTableView[T <: Expression](
    offsetRow: Int, offsetColumn: Int, wrap: TableView[T]
) extends TableView[T] {
    override def get(pos: TableCellPosition): Option[TableCell[T]] = wrap.get(
        TableCellPosition(pos.row + offsetRow, pos.column + offsetColumn)
    )
    override def lastRow: Option[Int] = wrap.lastRow.map(_ - offsetRow)
    override def lastColumn: Option[Int] = wrap.lastColumn.map(_ - offsetColumn)
    override def nonEmptyPositions: Iterable[TableCellPosition] = wrap.nonEmptyPositions.filter(
        pos => pos.row >= offsetRow && pos.column >= offsetColumn
    ).map(
        pos => TableCellPosition(pos.row - offsetRow, pos.column - offsetColumn)
    )
    override def hasHeaderRow: Boolean = offsetRow == 0 && wrap.hasHeaderRow
    override def hasHeaderColumn: Boolean = offsetColumn == 0 && wrap.hasHeaderColumn
}
