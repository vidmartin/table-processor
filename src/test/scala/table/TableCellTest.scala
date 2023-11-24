
package table

import org.scalatest.FunSuite
import expression._

class TableCellTest extends FunSuite {
    test("test1") {
        assert(TableCell.parse("assdjjjkhj") == Some(TableCell(StringExpression("assdjjjkhj"))))
        assert(TableCell.parse("@") == Some(TableCell(StringExpression("@"))))
        assert(TableCell.parse("231.123") match {
            case Some(TableCell(FloatExpression(v))) => (v - 231.123d).abs <= 0.000001
            case _ => false
        })
    }

    test("test2") {
        assert(TableCell.parse("").isEmpty)
        assert(TableCell.parse("    ").isEmpty)
        assert(TableCell.parse("1234") == Some(TableCell(IntExpression(1234))))
        assert(TableCell.parse("=53*C9") == Some(TableCell(
            MultiplyExpression(
                IntExpression(53),
                ReferenceExpression(
                    TableCellPosition(8, 2)
                )
        ))))
        assert(TableCell.parse("=D2 + C3*\"hello\"") == Some(TableCell(
            AddExpression(
                ReferenceExpression(
                    TableCellPosition(1, 3)
                ),
                MultiplyExpression(
                    ReferenceExpression(
                        TableCellPosition(2, 2)
                    ),
                    StringExpression("hello")
                )
            )
        )))
    }
}