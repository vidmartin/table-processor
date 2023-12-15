
package table

import expression.Expression

class RangeTableView[T <: Expression](underlying: TableView[T], range: TableCellRange) extends TableView[T] {
    override def getLocal(pos: TableCellPosition): Option[TableCell[T]] = {
        val underlyingPos = TableCellPosition(
            pos.row + range.upperLeft.row,
            pos.column + range.upperLeft.column,
        )

        if (range.contains(underlyingPos)) {
            underlying.getLocal(underlyingPos)
        } else {
            None
        }
    }
    override def lastLocalRow: Option[Int] = Some(range.lowerRight.row - range.upperLeft.row)
    override def lastLocalColumn: Option[Int] = Some(range.lowerRight.column - range.upperLeft.column)
    override def nonEmptyLocalPositions: Iterable[TableCellPosition] = underlying.nonEmptyLocalPositions.flatMap(
        pos => if (range.contains(pos)) {
            Some(
                TableCellPosition(
                    pos.row - range.upperLeft.row,
                    pos.column - range.upperLeft.column
                )
            )
        } else { None }
    )
    override def getGlobalRow(localRow: Int): Int = underlying.getGlobalRow(localRow + range.upperLeft.row)
    override def getGlobalColumn(localColumn: Int): Int = underlying.getGlobalColumn(localColumn + range.upperLeft.column)
}
