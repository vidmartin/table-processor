
package table

final case class TableCellRange(
    upperLeft: TableCellPosition,
    lowerRight: TableCellPosition,
) {
    if (upperLeft.column > lowerRight.column | upperLeft.row > lowerRight.row) {
        throw new IllegalArgumentException(f"the first position in TableCellRange not be to the right nor to the bottom of the second position!")
    }

    def iterRowsColumns: Iterable[TableCellPosition] = Iterable.range(upperLeft.row, lowerRight.row + 1).flatMap(
        i => Iterable.range(upperLeft.column, lowerRight.column + 1).map(
            j => TableCellPosition(i, j)
        )
    )

    def width = lowerRight.column + 1 - upperLeft.column
    def height = lowerRight.row + 1 - upperLeft.row

    def contains(pos: TableCellPosition): Boolean = {
        pos.column >= upperLeft.column &&
        pos.row >= upperLeft.row &&
        pos.column <= lowerRight.column &&
        pos.row <= lowerRight.row
    }
}
