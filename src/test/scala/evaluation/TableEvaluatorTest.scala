
package evaluation

import scala.collection.immutable.HashMap
import org.scalatest.FunSuite
import table._
import expression._

class TableEvaluatorTest extends FunSuite {
    val evaluators = Array(TopSortTableEvaluator)

    test("test1") {
        val positions = Array("B2", "D4", "E4", "E5", "F6", "Z8")
        val cells = Array[TableCell[Expression]](
            TableCell(IntExpression(7)),
            TableCell(IntExpression(6)),
            TableCell(
                SubtractExpression(
                    ReferenceExpression(TableCellPosition.parse("E5").get),
                    ReferenceExpression(TableCellPosition.parse("F6").get)
                )
            ),
            TableCell(
                MultiplyExpression(
                    ReferenceExpression(TableCellPosition.parse("B2").get),
                    ReferenceExpression(TableCellPosition.parse("D4").get)
                )
            ),
            TableCell(
                AddExpression(
                    ReferenceExpression(TableCellPosition.parse("B2").get),
                    ReferenceExpression(TableCellPosition.parse("D4").get)
                )
            )
        )

        val table = new Table[Expression](HashMap.from(
            positions.iterator.map(
                s => TableCellPosition.parse(s).get
            ).zip(cells)
        ))

        for (evaluator <- evaluators) {
            val result = evaluator.evaluateTable(table)

            assert(result.get(TableCellPosition.parse("A1").get).isEmpty)
            assert(result.get(TableCellPosition.parse("B2").get).get == TableCell(IntExpression(7)))
            assert(result.get(TableCellPosition.parse("D4").get).get == TableCell(IntExpression(6)))
            assert(result.get(TableCellPosition.parse("E4").get).get == TableCell(IntExpression(29)))
            assert(result.get(TableCellPosition.parse("E5").get).get == TableCell(IntExpression(42)))
            assert(result.get(TableCellPosition.parse("F6").get).get == TableCell(IntExpression(13)))
            assert(result.get(TableCellPosition.parse("Z8").get).isEmpty)
        }
    }

    test("test2") {
        val positions = Array("B2", "D4", "E4", "E5", "F6", "Z8")
        val cells = Array[TableCell[Expression]](
            new TableCell(IntExpression(7)),
            new TableCell(IntExpression(6)),
            new TableCell(
                SubtractExpression(
                    ReferenceExpression(TableCellPosition.parse("E5").get),
                    ReferenceExpression(TableCellPosition.parse("F6").get)
                )
            ),
            new TableCell(
                MultiplyExpression(
                    ReferenceExpression(TableCellPosition.parse("B2").get),
                    ReferenceExpression(TableCellPosition.parse("D4").get)
                )
            ),
            new TableCell(
                AddExpression(
                    ReferenceExpression(TableCellPosition.parse("E4").get),
                    ReferenceExpression(TableCellPosition.parse("D4").get)
                )
            )
        )

        val table = new Table[Expression](HashMap.from(
            positions.iterator.map(
                s => TableCellPosition.parse(s).get
            ).zip(cells)
        ))

        for (evaluator <- evaluators) {
            assertThrows[CircularReferenceException] {
                evaluator.evaluateTable(table)
            }
        }
    }

    test("test3") {
        val table = new Table[Expression](HashMap(
            (TableCellPosition(0, 0), new TableCell(
                ReferenceExpression(TableCellPosition(0, 0))
            )
        )))

        for (evaluator <- evaluators) {
            assertThrows[CircularReferenceException] {
                evaluation.TopSortTableEvaluator.evaluateTable(table)
            }
        }
    }
}
