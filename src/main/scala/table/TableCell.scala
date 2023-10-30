
package table

abstract class TableCell {
    def evaluate(referenceResolver: TableCellPosition => TableCell): TableCell
}

object EmptyTableCell extends TableCell {
    override def evaluate(referenceResolver: TableCellPosition => TableCell): TableCell = this
}

case class IntegerTableCell(value: Int) extends TableCell{
    override def evaluate(referenceResolver: TableCellPosition => TableCell): TableCell = this
}

case class FormulaTableCell(formula: String) extends TableCell {
    override def evaluate(referenceResolver: TableCellPosition => TableCell): TableCell = ???
}

object TableCell {
    def parse(s: String): Option[TableCell] = {
        if (s.isBlank()) {
            Some(EmptyTableCell)
        } else if (s.iterator.forall(c => c.isDigit)) {
            Some(IntegerTableCell(s.toInt))
        } else if (s.startsWith("=")) {
            Some(FormulaTableCell(s.substring(1)))
        } else {
            None
        }
    }
}
