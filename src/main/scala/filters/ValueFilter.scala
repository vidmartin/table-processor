
package filters

import expression.ConstantExpression
import table.TableView
import table.TableCellPosition
import table.TableCell
import expression.EmptyExpression

final case class ValueFilter(
    column: Int,
    comparator: Comparator,
    value: ConstantExpression
) extends RowFilter[ConstantExpression] {
    override def evaluate(row: Int, table: TableView[ConstantExpression]): Boolean = {
        evalCell(table.getLocal(TableCellPosition(row, column)))
    }

    private def evalCell(cell: TableCell[ConstantExpression]): Boolean = {
        comparator match {
            case Comparator.EQ => cell.expr == value
            case Comparator.NE => cell.expr != value
            case Comparator.LT => evalLessThan(cell.expr, value, false)
            case Comparator.GT => evalLessThan(value, cell.expr, false)
            case Comparator.LE => evalLessThan(cell.expr, value, true)
            case Comparator.GE => evalLessThan(value, cell.expr, true)
            case _ => throw new NotImplementedError()
        }
    }

    private def evalLessThan(lhs: ConstantExpression, rhs: ConstantExpression, orEqual: Boolean): Boolean = {
        (lhs.getInt, rhs.getInt) match {
            case (Some(a), Some(b)) => a < b || (orEqual && a == b)
            case _ => (lhs.getFloat, rhs.getFloat) match {
                case (Some(a), Some(b)) => a < b || (orEqual && a == b)
                case _ => false
            }
        }
    }
}
