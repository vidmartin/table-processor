package expression

import table.TableCellRange
import table.{TableCellPosition, TableCellValue}

final case class RangeExpression(range: TableCellRange) extends Expression {
    override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = {
        throw new Exception() // TODO: more specific exception
    }
}
