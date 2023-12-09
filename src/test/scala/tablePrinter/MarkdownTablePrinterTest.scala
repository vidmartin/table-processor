
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
import scala.util.matching.Regex
import filters.MultiFilter

class MarkdownTablePrinterTest extends FunSuite {
    test("test1 (empty table)") {
        val table = new Table[ConstantExpression](new HashMap())
        val printer = new MarkdownTablePrinter(ConstantExpressionFormatter)
        val writer = new InMemoryStringWriter()
        printer.printTable(table, writer, new MultiFilter(List.empty))

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
        val printer = new MarkdownTablePrinter(ConstantExpressionFormatter)
        val writer = new InMemoryStringWriter()
        printer.printTable(table, writer, new MultiFilter(List.empty))

        val lines = writer.get.linesIterator.toArray
        assert(lines.length == 4 || (lines.length == 5 && lines(4).forall(_.isWhitespace)))
        
        val row1cells = lines(2).split(Regex.quote("|"))
        assert(row1cells.length == 3)
        assert(row1cells(0) == "78")
        assert(row1cells(1) == "&nbsp;")
        assert(row1cells(2) == "hello-world")

        val row2cells = lines(3).split(Regex.quote("|"))
        assert(row2cells.length == 3)
        assert(row2cells(0) == "&nbsp;")
        assert(row2cells(1) == "42")
        assert(row2cells(2) == "34.56")
    }
}
