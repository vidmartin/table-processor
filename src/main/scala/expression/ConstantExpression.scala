package expression

import table.TableCellValue
import table.TableCellPosition

final case class ConstantExpression(value: TableCellValue) extends Expression {
    override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = value
}
