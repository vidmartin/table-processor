
package table

import expression.Expression
import expression.EmptyExpression

/** an interface providing random access to tabular data */
abstract class TableView[T >: EmptyExpression.type <: Expression] {
    /** read a cell from the given position relative to this view */
    def getLocal(pos: TableCellPosition): TableCell[T]
    /** the index of the last row in this view */
    def lastLocalRow: Option[Int]
    /** this index of the last column in this view */
    def lastLocalColumn: Option[Int]
    /** the positions of all non-empty cells in this view */
    def nonEmptyLocalPositions: Iterable[TableCellPosition]
    /** convert the index of the row relative to this view to a global index (relative to the underlying table) */
    def getGlobalRow(localRow: Int): Int
    /** convert the index of the column relative to this view to a global index (relative to the underlying table) */
    def getGlobalColumn(localColumn: Int): Int

    /** convert a position relative to this view to a global position (relative to the underlying table) */
    final def getGlobalPos(localPos: TableCellPosition): TableCellPosition = {
        TableCellPosition(
            getGlobalRow(localPos.row),
            getGlobalColumn(localPos.column),
        )
    }
}
