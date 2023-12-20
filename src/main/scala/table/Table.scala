
package table

import scala.collection.immutable.HashMap
import tableReader.TableReader
import expression.Expression
import expression.EmptyExpression
import expression.ConstantExpression

/** an immutable container for tabular data implementing the TableView interface */
class Table[T >: EmptyExpression.type <: Expression](content: HashMap[TableCellPosition, TableCell[T]]) extends TableView[T] {
    def getLocal(pos: TableCellPosition): TableCell[T] = {
        content.get(pos).getOrElse(TableCell(EmptyExpression.asInstanceOf[T]))
    }

    def withSet(pos: TableCellPosition, cell: TableCell[T]) = new Table(content.updated(pos, cell))
    def nonEmptyLocalPositions: Iterable[TableCellPosition] = content.filter {
        case (pos, TableCell(expr)) => !expr.isEmpty
    }.map {
        case (pos, cell) => pos
    }
    lazy val lastLocalRow: Option[Int] = content.keys.iterator.map(pos => pos.row).maxOption
    lazy val lastLocalColumn: Option[Int] = content.keys.iterator.map(pos => pos.column).maxOption
    override def getGlobalColumn(localColumn: Int): Int = localColumn
    override def getGlobalRow(localRow: Int): Int = localRow

    /** get all relevant positions - this returns a superset of whatever is returned by nonEmptyLocalPositions.
     * For each entry in nonEmptyLocalPositions, all cells referenced by this entry are also included (including empty cells). */
    final def allRelevantLocalPositions: Iterable[TableCellPosition] = {
        nonEmptyLocalPositions.flatMap(
            pos => Iterable.concat(
                List(pos),
                getLocal(pos).expr.referencedPositions
            )
        ).toSet
    }
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

    def empty[T >: ConstantExpression <: Expression] = new Table[T](HashMap.empty)
}
