
package table

abstract class TableCellValue
object EmptyTableCellValue extends TableCellValue
final case class IntegerTableCellValue(value: Int) extends TableCellValue
