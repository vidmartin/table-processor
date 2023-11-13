package expression

import table.{TableCellPosition, TableCellValue}

final case class ReferenceExpression(pos: TableCellPosition) extends Expression {
    override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = {
        referenceResolver(pos)
    }
}
