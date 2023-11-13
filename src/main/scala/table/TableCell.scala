
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
            None
        } else if (s.startsWith("=")) {
            Some(FormulaTableCell(s.substring(1)))
        } else {
            (s.toIntOption, s.toFloatOption) match {
                case (None, Some(v)) => Some(ValueTableCell(FloatTableCellValue(v)))
                case (Some(v), Some(_)) => Some(ValueTableCell(IntegerTableCellValue(v)))
                case _ => Some(ValueTableCell(StringTableCellValue(s)))
            }
        }
    }
}
