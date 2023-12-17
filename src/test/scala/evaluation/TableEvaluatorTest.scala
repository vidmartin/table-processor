
package evaluation

import scala.collection.immutable.HashMap
import org.scalatest.FunSuite
import table._
import expression._

abstract class TableEvaluatorTest extends FunSuite {
    def evaluator: BaseTableEvaluator

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

        val result = evaluator.evaluateTable(table)

        assert(result.getLocal(TableCellPosition.parse("A1").get).isEmpty)
        assert(result.getLocal(TableCellPosition.parse("B2").get).get == TableCell(IntExpression(7)))
        assert(result.getLocal(TableCellPosition.parse("D4").get).get == TableCell(IntExpression(6)))
        assert(result.getLocal(TableCellPosition.parse("E4").get).get == TableCell(IntExpression(29)))
        assert(result.getLocal(TableCellPosition.parse("E5").get).get == TableCell(IntExpression(42)))
        assert(result.getLocal(TableCellPosition.parse("F6").get).get == TableCell(IntExpression(13)))
        assert(result.getLocal(TableCellPosition.parse("Z8").get).isEmpty)
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

        assertThrows[CircularReferenceException] {
            evaluator.evaluateTable(table)
        }
    }

    test("test3") {
        val table = new Table[Expression](HashMap(
            (TableCellPosition(0, 0), new TableCell(
                ReferenceExpression(TableCellPosition(0, 0))
            )
        )))

        assertThrows[CircularReferenceException] {
            evaluator.evaluateTable(table)
        }
    }

    test("test4 (fail if referenced cell is empty)") {
        val table = new Table[Expression](HashMap(
            TableCellPosition.parse("A1").get -> TableCell(EmptyExpression),
            TableCellPosition.parse("B1").get -> TableCell(
                AddExpression(
                    ReferenceExpression(TableCellPosition.parse("A1").get),
                    IntExpression(1)
                )
            )
        ))

        assertThrows[Exception] {
            // TODO: more specific exception
            evaluator.evaluateTable(table)
        }
    }

    test("test5 (fail if referenced cell is unspecified)") {
        val table = new Table[Expression](HashMap(
            TableCellPosition.parse("B1").get -> TableCell(
                AddExpression(
                    ReferenceExpression(TableCellPosition.parse("A1").get),
                    IntExpression(1)
                )
            )
        ))

        assertThrows[Exception] {
            // TODO: more specific exception
            evaluator.evaluateTable(table)
        }
    }

    test("test6 (plain reference to empty cell)") {
        val table = new Table[Expression](HashMap(
            TableCellPosition.parse("A1").get -> TableCell(EmptyExpression),
            TableCellPosition.parse("B1").get -> TableCell(
                ReferenceExpression(TableCellPosition.parse("A1").get),
            ),
            TableCellPosition.parse("B2").get -> TableCell(
                ReferenceExpression(TableCellPosition.parse("A2").get),
            )
        ))

        val result = evaluator.evaluateTable(table)

        for (cellname <- List("B1", "B2")) {
            assert(
                result.getLocal(
                    TableCellPosition.parse(cellname).get
                ).map(
                    cell => cell == TableCell(EmptyExpression)
                ).getOrElse(true)
            )
        }
    }
}
