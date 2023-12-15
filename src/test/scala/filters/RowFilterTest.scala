
package filters

import org.scalatest.FunSuite
import table.Table
import scala.collection.immutable.HashMap
import table.TableCellPosition
import expression.IntExpression
import table.TableCell
import expression.StringExpression
import expression.FloatExpression
import expression.ConstantExpression
import expression.EmptyExpression

class RowFilterTest extends FunSuite {
    val table = new Table[ConstantExpression](HashMap(
        TableCellPosition.parse("A1").get -> TableCell(IntExpression(78)),
        TableCellPosition.parse("C1").get -> TableCell(StringExpression("hello-world")),

        TableCellPosition.parse("B2").get -> TableCell(IntExpression(42)),
        TableCellPosition.parse("C2").get -> TableCell(FloatExpression(34.56)),

        TableCellPosition.parse("A3").get -> TableCell(IntExpression(52)),
        TableCellPosition.parse("B3").get -> TableCell(IntExpression(70)),
        TableCellPosition.parse("C3").get -> TableCell(FloatExpression(23.45)),
        TableCellPosition.parse("D3").get -> TableCell(StringExpression("goodbye")),

        TableCellPosition.parse("A4").get -> TableCell(IntExpression(65)),
        TableCellPosition.parse("B4").get -> TableCell(EmptyExpression),
        TableCellPosition.parse("C4").get -> TableCell(IntExpression(30)),
        TableCellPosition.parse("D4").get -> TableCell(StringExpression("a b c d")),
    ))

    test("test1") {
        val filter = ValueFilter(0, Comparator.GT, IntExpression(60))
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(true, false, false, true)
        )
    }

    test("test2") {
        val filter = ValueFilter(1, Comparator.GT, IntExpression(60))
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(false, false, true, false)
        )
    }

    test("test3") {
        val filter = EmptinessFilter(1, false)
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(false, true, true, false)
        )
    }

    test("test4") {
        val filter = ValueFilter(2, Comparator.LE, IntExpression(30))
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(false, false, true, true)
        )
    }

    test("test5") {
        val filter = ValueFilter(2, Comparator.LT, IntExpression(30))
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(false, false, true, false)
        )
    }

    test("test6") {
        val filter = ValueFilter(2, Comparator.EQ, StringExpression("hello-world"))
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(true, false, false, false)
        )
    }

    test("test7") {
        val filter = ValueFilter(3, Comparator.EQ, StringExpression("hello-world"))
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(false, false, false, false)
        )
    }

    test("test8") {
        val filter = ValueFilter(3, Comparator.EQ, StringExpression("goodbye"))
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(false, false, true, false)
        )
    }

    test("test9") {
        val filter = EmptinessFilter(3, true)
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(true, true, false, false)
        )
    }

    test("test10") {
        val filter = EmptinessFilter(4, false)
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(false, false, false, false)
        )
    }

    test("test11") {
        val filter = new AndFilter[ConstantExpression](List())
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(true, true, true, true)
        )
    }

    test("test12") {
        val filter = new AndFilter[ConstantExpression](List(
            ValueFilter(0, Comparator.GT, IntExpression(60))
        ))
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(true, false, false, true)
        )
    }

    test("test13") {
        val filter = new AndFilter[ConstantExpression](List(
            ValueFilter(0, Comparator.GT, IntExpression(60)),
            ValueFilter(1, Comparator.LT, IntExpression(100))
        ))
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(false, false, false, false)
        )
    }

    test("test14") {
        val filter = new AndFilter[ConstantExpression](List(
            ValueFilter(0, Comparator.GT, IntExpression(60)),
            ValueFilter(2, Comparator.LT, IntExpression(100))
        ))
        assert(
            Iterable.range(0, table.lastRow.get + 1).map(
                row => filter.evaluate(row, table)
            ).toList == List(false, false, false, true)
        )
    }    
}
