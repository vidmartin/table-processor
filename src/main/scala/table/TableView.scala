
package table

import expression.Expression
import expression.EmptyExpression

abstract class TableView[T >: EmptyExpression.type <: Expression] {
    def getLocal(pos: TableCellPosition): TableCell[T]
    def lastLocalRow: Option[Int]
    def lastLocalColumn: Option[Int]
    def nonEmptyLocalPositions: Iterable[TableCellPosition]
    def getGlobalRow(localRow: Int): Int
    def getGlobalColumn(localColumn: Int): Int

    def getGlobalPos(localPos: TableCellPosition): TableCellPosition = {
        TableCellPosition(
            getGlobalRow(localPos.row),
            getGlobalColumn(localPos.column),
        )
    }
    def allRelevantLocalPositions: Iterable[TableCellPosition] = {
        nonEmptyLocalPositions.flatMap(
            pos => Iterable.concat(
                List(pos),
                getLocal(pos).expr.referencedPositions
            )
        ).toSet
    }
}
