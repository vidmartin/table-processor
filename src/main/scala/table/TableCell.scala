
package table

abstract class TableCell {
    def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue
    def dependsOn: Iterable[TableCellPosition]
}

final case class ValueTableCell(value: TableCellValue) extends TableCell {
    override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = value
    override def dependsOn: Iterable[TableCellPosition] = Iterable.empty
}

final case class FormulaTableCell(formula: String) extends TableCell {
    override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = ???
    override def dependsOn: Iterable[TableCellPosition] = ???
}

object TableCell {
    def parse(s: String): Option[TableCell] = {
        if (s.forall(c => c.isWhitespace)) {
            Some(ValueTableCell(EmptyTableCellValue))
        } else if (s.iterator.forall(c => c.isDigit)) {
            Some(ValueTableCell(IntegerTableCellValue(s.toInt)))
        } else if (s.startsWith("=")) {
            Some(FormulaTableCell(s.substring(1)))
        } else {
            None
        }
    }
}
