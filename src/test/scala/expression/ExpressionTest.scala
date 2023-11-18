package expression

import org.scalatest.FunSuite
import table.TableCellPosition

class ExpressionTest extends FunSuite {
    object MockExpressionEvaluationContext extends ExpressionEvaluationContext {
        override def get(pos: TableCellPosition): Expression = IntExpression(pos.row + 1)
    }

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
            ).evaluate(MockExpressionEvaluationContext)
            == IntExpression(22)
        )
    }

    test("test2") {
        assert(
            MultiplyExpression(
                AddExpression(
                    ReferenceExpression(TableCellPosition.parse("A5").get),
                    ReferenceExpression(TableCellPosition.parse("B6").get)
                ),
                SubtractExpression(
                    ReferenceExpression(TableCellPosition.parse("F4").get),
                    ReferenceExpression(TableCellPosition.parse("H2").get)
                )
            ).evaluate(MockExpressionEvaluationContext)
            == IntExpression(22)
        )
    }

    test("test3") {
        assert(
            AddExpression(
                StringExpression("hello"),
                AddExpression(
                    StringExpression(" "),
                    StringExpression("world!")
                )
            ).evaluate(MockExpressionEvaluationContext)
            == StringExpression("hello world!")
        )
    }

    test("test4") {
        assert(
            ModuloExpression(
                IntExpression(123),
                IntExpression(10)
            ).evaluate(MockExpressionEvaluationContext)
            == IntExpression(3)
        )

        assert(
            DivideExpression(
                IntExpression(123),
                IntExpression(10)
            ).evaluate(MockExpressionEvaluationContext)
            == FloatExpression(12.3)
        )

        assert(
            DivideExpression(
                IntExpression(120),
                IntExpression(10)
            ).evaluate(MockExpressionEvaluationContext)
            == IntExpression(12)
        )

        assert(
            DivideExpression(
                FloatExpression(123),
                IntExpression(10)
            ).evaluate(MockExpressionEvaluationContext)
            == FloatExpression(12.3)
        )

        assert(
            DivideExpression(
                FloatExpression(120),
                IntExpression(10)
            ).evaluate(MockExpressionEvaluationContext)
            == FloatExpression(12)
        )
    }

    test("test5") {
        // TODO: more specific exceptions!

        assertThrows[Exception] {
            DivideExpression(
                IntExpression(10),
                StringExpression("asdsad")
            ).evaluate(MockExpressionEvaluationContext)
        }

        assertThrows[Exception] {
            ModuloExpression(
                ReferenceExpression(TableCellPosition.parse("C4").get),
                StringExpression("asdsad")
            ).evaluate(MockExpressionEvaluationContext)
        }
    }
}
