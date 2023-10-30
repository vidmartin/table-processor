
package table

abstract class TableCell
object EmptyTableCell extends TableCell
case class IntegerTableCell(val value: Int) extends TableCell
case class FormulaTableCell(val formula: String) extends TableCell

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
