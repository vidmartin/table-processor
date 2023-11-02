
package evaluation

import scala.collection.immutable.HashMap
import org.scalatest.FunSuite
import table._

class TableEvaluatorTest extends FunSuite {
    val evaluators = Array(TableEvaluator)

    test("test1") {
        val positions = Array("B2", "D4", "E4", "E5", "F6", "Z8")
        val cells = Array(
            ValueTableCell(IntegerTableCellValue(7)),
            ValueTableCell(IntegerTableCellValue(6)),
            new TableCell {
                override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = {
                    val a = referenceResolver(TableCellPosition.parse("E5").get)
                    val b = referenceResolver(TableCellPosition.parse("F6").get)
                    (a, b) match {
                        case (IntegerTableCellValue(ai), IntegerTableCellValue(bi)) => IntegerTableCellValue(ai - bi)
                    }
                }
                override def dependsOn = Array(TableCellPosition.parse("E5").get, TableCellPosition.parse("F6").get)
            },
            new TableCell {
                override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = {
                    val a = referenceResolver(TableCellPosition.parse("B2").get)
                    val b = referenceResolver(TableCellPosition.parse("D4").get)
                    (a, b) match {
                        case (IntegerTableCellValue(ai), IntegerTableCellValue(bi)) => IntegerTableCellValue(ai * bi)
                    }
                }
                override def dependsOn = Array(TableCellPosition.parse("B2").get, TableCellPosition.parse("D4").get)
            },
            new TableCell {
                override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = {
                    val a = referenceResolver(TableCellPosition.parse("B2").get)
                    val b = referenceResolver(TableCellPosition.parse("D4").get)
                    (a, b) match {
                        case (IntegerTableCellValue(ai), IntegerTableCellValue(bi)) => IntegerTableCellValue(ai + bi)
                    }
                }
                override def dependsOn = Array(TableCellPosition.parse("B2").get, TableCellPosition.parse("D4").get)
            },
            ValueTableCell(EmptyTableCellValue),
        )

        val table = new Table[TableCell](HashMap.from(
            positions.iterator.map(
                s => TableCellPosition.parse(s).get
            ).zip(cells)
        ))

        for (evaluator <- evaluators) {
            val result = evaluator.evaluateTable(table)

            assert(result.get(TableCellPosition.parse("A1").get) == ValueTableCell(EmptyTableCellValue))
            assert(result.get(TableCellPosition.parse("B2").get) == ValueTableCell(IntegerTableCellValue(7)))
            assert(result.get(TableCellPosition.parse("D4").get) == ValueTableCell(IntegerTableCellValue(6)))
            assert(result.get(TableCellPosition.parse("E4").get) == ValueTableCell(IntegerTableCellValue(29)))
            assert(result.get(TableCellPosition.parse("E5").get) == ValueTableCell(IntegerTableCellValue(42)))
            assert(result.get(TableCellPosition.parse("F6").get) == ValueTableCell(IntegerTableCellValue(13)))
            assert(result.get(TableCellPosition.parse("Z8").get) == ValueTableCell(EmptyTableCellValue))
        }
    }

    test("test2") {
        val positions = Array("B2", "D4", "E4", "E5", "F6", "Z8")
        val cells = Array(
            ValueTableCell(IntegerTableCellValue(7)),
            ValueTableCell(IntegerTableCellValue(6)),
            new TableCell {
                override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = {
                    val a = referenceResolver(TableCellPosition.parse("E5").get)
                    val b = referenceResolver(TableCellPosition.parse("F6").get)
                    (a, b) match {
                        case (IntegerTableCellValue(ai), IntegerTableCellValue(bi)) => IntegerTableCellValue(ai - bi)
                    }
                }
                override def dependsOn = Array(TableCellPosition.parse("E5").get, TableCellPosition.parse("F6").get)
            },
            new TableCell {
                override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = {
                    val a = referenceResolver(TableCellPosition.parse("B2").get)
                    val b = referenceResolver(TableCellPosition.parse("D4").get)
                    (a, b) match {
                        case (IntegerTableCellValue(ai), IntegerTableCellValue(bi)) => IntegerTableCellValue(ai * bi)
                    }
                }
                override def dependsOn = Array(TableCellPosition.parse("B2").get, TableCellPosition.parse("D4").get)
            },
            new TableCell {
                override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = {
                    val a = referenceResolver(TableCellPosition.parse("E4").get)
                    val b = referenceResolver(TableCellPosition.parse("D4").get)
                    (a, b) match {
                        case (IntegerTableCellValue(ai), IntegerTableCellValue(bi)) => IntegerTableCellValue(ai + bi)
                    }
                }
                override def dependsOn = Array(TableCellPosition.parse("E2").get, TableCellPosition.parse("D4").get)
            },
            ValueTableCell(EmptyTableCellValue),
        )

        val table = new Table[TableCell](HashMap.from(
            positions.iterator.map(
                s => TableCellPosition.parse(s).get
            ).zip(cells)
        ))

        for (evaluator <- evaluators) {
            assertThrows[Exception /* TODO: more specific */] {
                evaluator.evaluateTable(table)
            }
        }
    }

    test("test3") {
        val table = new Table[TableCell](HashMap(
            (TableCellPosition(0, 0), new TableCell {
                override def evaluate(referenceResolver: TableCellPosition => TableCellValue): TableCellValue = {
                    referenceResolver(TableCellPosition(0, 0))
                }
                override def dependsOn: Iterable[TableCellPosition] = Array(TableCellPosition(0, 0))
            })
        ))

        for (evaluator <- evaluators) {
            assertThrows[Exception /* TODO: more specific */] {
                evaluation.TableEvaluator.evaluateTable(table)
            }
        }
    }
}
