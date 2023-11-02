
package table

import scala.collection.immutable.HashMap
import tableReader.TableReader

class Table[T <: TableCell](content: HashMap[TableCellPosition, T]) {
    def get(pos: TableCellPosition): Option[T] = {
        content.get(pos)
    }

    def withSet(pos: TableCellPosition, cell: T) = new Table(content.updated(pos, cell))
    def nonEmptyPositions: Iterable[TableCellPosition] = content.keys
}

object Table {
    def parse(tableReader: TableReader): Table[TableCell] = {
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

    def empty[T <: TableCell] = new Table[T](HashMap.empty)
}
