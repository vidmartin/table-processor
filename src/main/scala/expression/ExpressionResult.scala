package expression

import table.TableCellValue
import table.TableCellPosition
import table.TableCellRange

abstract class ExpressionResult {
    def width: Int
    def values(referenceResolver: TableCellPosition => TableCellValue): Array[TableCellValue]
    def value(referenceResolver: TableCellPosition => TableCellValue): Option[TableCellValue] = {
        val vals = values(referenceResolver)
        return if (vals.length == 1) {
            Some(vals(0))
        } else {
            None
        }
    }
}

final case class ValueExpressionResult(value: TableCellValue) extends ExpressionResult {
    override def width: Int = 1
    override def values(referenceResolver: TableCellPosition => TableCellValue): Array[TableCellValue] = Array(value)
}

final case class RangeExpressionResult(range: TableCellRange) extends ExpressionResult {
    override def width: Int = range.lowerRight.column - range.upperLeft.column + 1
    override def values(referenceResolver: TableCellPosition => TableCellValue): Array[TableCellValue] = {
        Iterable.range(range.upperLeft.row, range.lowerRight.row + 1).flatMap(
            i => Iterable.range(range.upperLeft.column, range.lowerRight.column + 1).map(
                j => referenceResolver(
                    TableCellPosition(range.upperLeft.row + i, range.upperLeft.column + j)
                )
            )
        ).toArray
    }
}