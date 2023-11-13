
package expression

import table.TableCellValue
import table.TableCellPosition
import table.IntegerTableCellValue
import function.BinaryFunction

abstract class BinaryOperator extends Expression {
    def lhs: Expression
    def rhs: Expression
}

abstract class BaseFunctionBinaryOperator extends BinaryOperator {
    override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = {
        this.func.evaluate(
            lhs.evaluate(referenceResolver),
            rhs.evaluate(referenceResolver)
        )
    }

    def func: function.BinaryFunction
}

final case class FunctionBinaryOperator(
    override val lhs: Expression,
    override val rhs: Expression,
    override val func: function.BinaryFunction
) extends BaseFunctionBinaryOperator
