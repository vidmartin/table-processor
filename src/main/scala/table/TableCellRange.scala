
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

    def iterRowsColumns: Iterable[TableCellPosition] = Iterable.range(upperLeft.row, lowerRight.row + 1).flatMap(
        i => Iterable.range(upperLeft.column, lowerRight.column + 1).map(
            j => TableCellPosition(i, j)
        )
    )

    def width = lowerRight.column + 1 - upperLeft.column
    def height = lowerRight.row + 1 - upperLeft.row
}
