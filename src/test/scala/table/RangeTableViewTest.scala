
package table

import org.scalatest.FunSuite
import scala.collection.immutable.HashMap
import expression.IntExpression

class RangeTableViewTest extends FunSuite {
    val table = new Table(HashMap.from(
        Iterable.range(0, 5).flatMap(
            i => Iterable.range(0, 5).map(
                j => TableCellPosition(i, j) -> TableCell(
                    IntExpression(i * 5 + j + 1)
                )
            )
        )
    ))


    test("test1 (2x2 range)") {
        val range = TableCellRange(
            TableCellPosition.parse("B2").get,
            TableCellPosition.parse("C3").get,
        )

        val view = new RangeTableView(table, range)

        assert(view.getLocal(TableCellPosition.parse("A1").get).get.expr == IntExpression(7))
        assert(view.getLocal(TableCellPosition.parse("B2").get).get.expr == IntExpression(13))
        assert(view.getLocal(TableCellPosition.parse("C3").get).isEmpty)
        assert(
            view.nonEmptyLocalPositions.map(
                pos => view.getLocal(pos).get.expr.value
            ).fold(0)((a, b) => a + b) == 40
        )
        assert(view.lastLocalColumn == Some(1))
        assert(view.lastLocalRow == Some(1))
    }

        test("test1 (3x10 range)") {
        val range = TableCellRange(
            TableCellPosition.parse("B2").get,
            TableCellPosition.parse("D11").get,
        )

        val view = new RangeTableView(table, range)

        assert(view.getLocal(TableCellPosition.parse("A1").get).get.expr == IntExpression(7))
        assert(view.getLocal(TableCellPosition.parse("B2").get).get.expr == IntExpression(13))
        assert(view.getLocal(TableCellPosition.parse("C3").get).get.expr == IntExpression(19))
        assert(
            view.nonEmptyLocalPositions.map(
                pos => view.getLocal(pos).get.expr.value
            ).fold(0)((a, b) => a + b) == 186
        )
        assert(view.lastLocalColumn == Some(2))
        assert(view.lastLocalRow == Some(9))
    }
}
