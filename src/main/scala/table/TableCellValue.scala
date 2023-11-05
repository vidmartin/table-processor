
package table

abstract class TableCellValue
final case class IntegerTableCellValue(value: Int) extends TableCellValue
final case class FloatTableCellValue(value: Float) extends TableCellValue
final case class StringTableCellValue(value: String) extends TableCellValue
