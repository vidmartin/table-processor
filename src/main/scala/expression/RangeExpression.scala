package expression

import table.TableCellRange
import table.{TableCellPosition, TableCellValue}

final case class RangeExpression(range: TableCellRange) extends Expression with IterableExpression[Expression] {
    def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = {
        throw new Exception() // TODO: more specific exception
    }
    def referencedPositions: Iterable[TableCellPosition] = {
        Iterable.range(range.upperLeft.row, range.lowerRight.row + 1).flatMap(
            i => Iterable.range(range.upperLeft.column, range.lowerRight.column + 1).map(
                j => TableCellPosition(i, j)
            )
        )
    }
    def elements(context: ExpressionEvaluationContext): Iterable[Expression] = referencedPositions.map(
        pos => context.get(pos)
    )
}

