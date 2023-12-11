
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
        val cellEmpty = table.get(TableCellPosition(row, column)) match {
            case None => true // TODO: make table.get return TableCell instead of Option[TableCell]
            case Some(TableCell(EmptyExpression)) => true
            case _ => false
        }
        return cellEmpty == empty
    }
}
