
package table

abstract class TableCellValue
object EmptyTableCellValue extends TableCellValue
case class IntegerTableCellValue(value: Int) extends TableCellValue
