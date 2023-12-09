
package filters

import expression.Expression
import table.TableView

class MultiFilter[T <: Expression](
    list: Iterable[RowFilter[T]]
) extends RowFilter[T] {
    override def evaluate(row: Int, table: TableView[T]): Boolean = {
        list.isEmpty || list.forall(filter => filter.evaluate(row, table))
    }
}
