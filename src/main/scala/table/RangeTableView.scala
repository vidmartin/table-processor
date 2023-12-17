
package table

import expression.Expression
import expression.EmptyExpression

class RangeTableView[T >: EmptyExpression.type <: Expression](underlying: TableView[T], range: TableCellRange) extends TableView[T] {
    override def getLocal(pos: TableCellPosition): TableCell[T] = {
        val underlyingPos = TableCellPosition(
            pos.row + range.upperLeft.row,
            pos.column + range.upperLeft.column,
        )

        if (range.contains(underlyingPos)) {
            underlying.getLocal(underlyingPos)
        } else {
            TableCell(EmptyExpression)
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
