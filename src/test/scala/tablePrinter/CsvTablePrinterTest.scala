
package tablePrinter

import org.scalatest.FunSuite
import table.Table
import scala.collection.immutable.HashMap
import config.CsvConfig
import expression.ConstantExpression
import stringWriter.InMemoryStringWriter
import table.TableCellPosition
import expression.IntExpression
import table.TableCell
import expression.StringExpression
import expression.FloatExpression
import filters.AndFilter

class CsvTablePrinterTest extends FunSuite {
    test("test1 (empty table)") {
        val table = new Table[ConstantExpression](new HashMap())
        val printer = new CsvTablePrinter(new CsvConfig(","), ConstantExpressionFormatter)
        val writer = new InMemoryStringWriter()
        printer.printTable(
            TablePrintOptions(
                table, new AndFilter(Iterable.empty), false, false
            ), writer
        )

        val lines = writer.get.linesIterator.toArray
        assert(lines.length <= 2)
        assert(lines.forall(line => line.forall(_.isWhitespace)))
    }

    test("test2 (table with data)") {
        val table = new Table[ConstantExpression](HashMap(
            TableCellPosition.parse("A1").get -> TableCell(IntExpression(78)),
            TableCellPosition.parse("C1").get -> TableCell(StringExpression("hello-world")),
            TableCellPosition.parse("B2").get -> TableCell(IntExpression(42)),
            TableCellPosition.parse("C2").get -> TableCell(FloatExpression(34.56))
        ))
        val printer = new CsvTablePrinter(new CsvConfig(","), ConstantExpressionFormatter)
        val writer = new InMemoryStringWriter()
        printer.printTable(
            TablePrintOptions(
                table, new AndFilter(Iterable.empty), false, false
            ), writer
        )

        val lines = writer.get.linesIterator.toArray
        assert(lines.length == 2 || (lines.length == 3 && lines(2).forall(_.isWhitespace)))
        
        val row1cells = lines(0).split(",")
        assert(row1cells.length == 3)
        assert(row1cells(0) == "78")
        assert(row1cells(1).forall(_.isWhitespace))
        assert(row1cells(2) == "hello-world")

        val row2cells = lines(1).split(",")
        assert(row2cells.length == 3)
        assert(row2cells(0).forall(_.isWhitespace))
        assert(row2cells(1) == "42")
        assert(row2cells(2) == "34.56")
    }

    test("test3 (table with data and headers)") {
        val table = new Table[ConstantExpression](HashMap(
            TableCellPosition.parse("A1").get -> TableCell(IntExpression(78)),
            TableCellPosition.parse("C1").get -> TableCell(StringExpression("hello-world")),
            TableCellPosition.parse("B2").get -> TableCell(IntExpression(42)),
            TableCellPosition.parse("C2").get -> TableCell(FloatExpression(34.56))
        ))
        val printer = new CsvTablePrinter(new CsvConfig(","), ConstantExpressionFormatter)
        val writer = new InMemoryStringWriter()
        printer.printTable(
            TablePrintOptions(
                table, new AndFilter(Iterable.empty), true, true
            ), writer
        )

        val lines = writer.get.linesIterator.toArray
        assert(lines.length == 3 || (lines.length == 4 && lines(3).forall(_.isWhitespace)))

        val row0cells = lines(0).split(",")
        assert(row0cells.length == 4)
        assert(row0cells(0).forall(_.isWhitespace))
        assert(row0cells(1) == "A")
        assert(row0cells(2) == "B")
        assert(row0cells(3) == "C")
        
        val row1cells = lines(1).split(",")
        assert(row1cells.length == 4)
        assert(row1cells(0) == "1")
        assert(row1cells(1) == "78")
        assert(row1cells(2).forall(_.isWhitespace))
        assert(row1cells(3) == "hello-world")

        val row2cells = lines(2).split(",")
        assert(row2cells.length == 4)
        assert(row2cells(0) == "2")
        assert(row2cells(1).forall(_.isWhitespace))
        assert(row2cells(2) == "42")
        assert(row2cells(3) == "34.56")
    }
}
