
package table

import scala.collection.immutable.HashMap
import tableReader.TableReader
import expression.Expression

class Table[T <: Expression](content: HashMap[TableCellPosition, TableCell[T]]) extends TableView[T] {
    def get(pos: TableCellPosition): Option[TableCell[T]] = {
        content.get(pos)
    }

    def withSet(pos: TableCellPosition, cell: TableCell[T]) = new Table(content.updated(pos, cell))
    def nonEmptyPositions: Iterable[TableCellPosition] = content.keys
    def lastRow: Option[Int] = content.keys.iterator.map(pos => pos.row).maxOption
    def lastColumn: Option[Int] = content.keys.iterator.map(pos => pos.column).maxOption
}

object Table {
    def parse(tableReader: TableReader): Table[Expression] = {
        val map = HashMap.from(
            tableReader.zipWithIndex.flatMap({
                case (row, i) => row.iterator.zipWithIndex.flatMap({
                    case (cell, j) => TableCell.parse(cell).map(
                        parsed => (TableCellPosition(i, j), parsed)
                    )
                })
            })
        )

        new Table(map)
    }

    def empty[T <: Expression] = new Table[T](HashMap.empty)
}
