
package expression

import table.TableCellValue

abstract class BinaryOperator(
    val lhs: Expression,
    val rhs: Expression
) extends Expression

object BinaryOperator {
}
