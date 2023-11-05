
package table

final case class TableCellRange(
    upperLeft: TableCellPosition,
    lowerRight: TableCellPosition,
) {
    if (upperLeft.column > lowerRight.column) {
        throw new Exception() // TODO: more specific excepetion
    }

    if (upperLeft.row > lowerRight.row) {
        throw new Exception() // TODO: more specific excepetion
    }
}
