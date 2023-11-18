package expression

import org.scalatest.FunSuite
import table.TableCellPosition

class ExpressionTest extends FunSuite {
    test("test1") {
        assert(
            MultiplyExpression(
                AddExpression(
                    IntExpression(5),
                    IntExpression(6)
                ),
                SubtractExpression(
                    IntExpression(4),
                    IntExpression(2)
                )
            ).evaluate(
                new ExpressionEvaluationContext {
                    override def get(pos: TableCellPosition): Expression = {
                        throw new NotImplementedError() 
                    }
                }
            ) == IntExpression(22)
        )
    }
}
