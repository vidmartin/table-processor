
package table

abstract class TableCell
class EmptyTableCell() extends TableCell
class IntegerTableCell(val value: Int) extends TableCell
class FormulaTableCell(val formula: String) extends TableCell
