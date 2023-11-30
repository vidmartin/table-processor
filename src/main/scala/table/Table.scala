
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
    lazy val lastRow: Option[Int] = content.keys.iterator.map(pos => pos.row).maxOption
    lazy val lastColumn: Option[Int] = content.keys.iterator.map(pos => pos.column).maxOption
    override def hasHeaderRow: Boolean = false
    override def hasHeaderColumn: Boolean = false
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
