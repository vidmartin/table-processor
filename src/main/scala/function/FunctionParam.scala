
package function

import table.TableCellRange
import table.TableCellValue

abstract class FunctionParam
final case class FunctionParamValue(value: TableCellValue) extends FunctionParam
final case class FunctionParamRange(width: Int, array: Array[TableCellValue]) extends FunctionParam
