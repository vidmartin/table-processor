
package filters

import expression.Expression
import table.TableView

class AndFilter[T <: Expression](
    list: Iterable[RowFilter[T]]
) extends RowFilter[T] {
    override def evaluate(row: Int, table: TableView[T]): Boolean = {
        list.forall(filter => filter.evaluate(row, table))
    }
}
