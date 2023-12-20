
package filters

import expression.Expression
import table.TableView
import expression.EmptyExpression

/** combines multiple filters together - a row must pass all the wrapped filters to pass an AndFilter */
class AndFilter[T >: EmptyExpression.type <: Expression](
    list: Iterable[RowFilter[T]]
) extends RowFilter[T] {
    override def evaluate(row: Int, table: TableView[T]): Boolean = {
        list.forall(filter => filter.evaluate(row, table))
    }
}
