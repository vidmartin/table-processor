
package table

import expression.Expression

abstract class TableView[T <: Expression] {
    def getLocal(pos: TableCellPosition): Option[TableCell[T]]
    def lastLocalRow: Option[Int]
    def lastLocalColumn: Option[Int]
    def nonEmptyLocalPositions: Iterable[TableCellPosition]
    def getGlobalRow(localRow: Int): Int
    def getGlobalColumn(localColumn: Int): Int
}
