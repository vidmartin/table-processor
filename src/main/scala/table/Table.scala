
package table

import scala.collection.immutable.HashMap
import tableReader.TableReader
import expression.Expression

class Table[T <: Expression](content: HashMap[TableCellPosition, TableCell[T]]) extends TableView[T] {
    def getLocal(pos: TableCellPosition): Option[TableCell[T]] = {
        content.get(pos)
    }

    def withSet(pos: TableCellPosition, cell: TableCell[T]) = new Table(content.updated(pos, cell))
    def nonEmptyLocalPositions: Iterable[TableCellPosition] = content.keys
    lazy val lastLocalRow: Option[Int] = content.keys.iterator.map(pos => pos.row).maxOption
    lazy val lastLocalColumn: Option[Int] = content.keys.iterator.map(pos => pos.column).maxOption
    override def getGlobalColumn(localColumn: Int): Int = localColumn
    override def getGlobalRow(localRow: Int): Int = localRow
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
