
package tablePrinter

import org.scalatest.FunSuite
import table.Table
import tableReader.WrapperTableReader
import table.TableCell
import expression.StringExpression
import table.TableCellPosition
import expression.IntExpression

class TableViewWithHeadersTest extends FunSuite {
    test("test1") {
        val table = Table.parse(new WrapperTableReader(
            List(
                List("", "323", "22"),
                List("", "", "", "", "500"),
                List("123", "", "33"),
            ).iterator
        ))

        val view = new TableViewWithHeaders(table)

        assert(view.lastColumn == Some(5))
        assert(view.lastRow == Some(3))

        assert(view.get(TableCellPosition(0, 0)).isEmpty)
        assert(view.get(TableCellPosition(0, 1)) == Some(TableCell(StringExpression("A"))))
        assert(view.get(TableCellPosition(0, 3)) == Some(TableCell(StringExpression("C"))))
        assert(view.get(TableCellPosition(1, 0)) == Some(TableCell(StringExpression("1"))))
        assert(view.get(TableCellPosition(3, 0)) == Some(TableCell(StringExpression("3"))))
        assert(view.get(TableCellPosition(1, 2)) == Some(TableCell(IntExpression(323))))
        assert(view.get(TableCellPosition(3, 3)) == Some(TableCell(IntExpression(33))))
        assert(view.get(TableCellPosition(3, 2)).isEmpty)
    }
}
