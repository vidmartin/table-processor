
package table

import expression.Expression

abstract class TableView[T <: Expression] {
    def get(pos: TableCellPosition): Option[TableCell[T]]
    def lastRow: Option[Int]
    def lastColumn: Option[Int]
    def nonEmptyPositions: Iterable[TableCellPosition]
}
