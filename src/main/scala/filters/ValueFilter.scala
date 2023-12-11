
package filters

import expression.ConstantExpression
import table.TableView
import table.TableCellPosition
import table.TableCell

final case class ValueFilter(
    column: Int,
    comparator: Comparator,
    value: ConstantExpression
) extends RowFilter[ConstantExpression] {
    override def evaluate(row: Int, table: TableView[ConstantExpression]): Boolean = {
        table.get(TableCellPosition(row, column)) match {
            case None => false
            case Some(cell) => evalCell(cell)
        }
    }

    private def evalCell(cell: TableCell[ConstantExpression]): Boolean = {
        comparator match {
            case Comparator.EQ => cell.expr == value
            case Comparator.NE => cell.expr != value
            case Comparator.LT => evalLessThan(cell)
            case Comparator.GT => cell.expr != value && !evalLessThan(cell)
            case Comparator.LE => cell.expr == value || evalLessThan(cell)
            case Comparator.GE => !evalLessThan(cell)
            case _ => throw new NotImplementedError()
        }
    }

    private def evalLessThan(cell: TableCell[ConstantExpression]): Boolean = {
        (cell.expr.getInt, value.getInt) match {
            case (Some(a), Some(b)) => a < b
            case _ => (cell.expr.getFloat, value.getFloat) match {
                case (Some(a), Some(b)) => a < b
                case _ => false
            }
        }
    }
}
