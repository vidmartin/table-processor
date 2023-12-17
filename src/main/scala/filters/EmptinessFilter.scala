
package filters

import expression.ConstantExpression
import table.TableView
import table.TableCellPosition
import expression.EmptyExpression
import table.TableCell

final case class EmptinessFilter(
    column: Int,
    empty: Boolean
) extends RowFilter[ConstantExpression] {
    override def evaluate(row: Int, table: TableView[ConstantExpression]): Boolean = {
        val cellEmpty = table.getLocal(TableCellPosition(row, column)) match {
            case TableCell(EmptyExpression) => true
            case _ => false
        }
        return cellEmpty == empty
    }
}
