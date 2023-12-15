
package table

import org.scalatest.FunSuite
import tableReader.TableReader
import tableReader.WrapperTableReader
import expression._

class TableTest extends FunSuite {
    test("test1") {
        val table = Table.parse(new WrapperTableReader(
            List(
                List("", "323", "22"),
                List("", "", "", "", "500"),
                List("123", "", "33"),
            ).iterator
        ))

        assert(table.lastLocalColumn == Some(4))
        assert(table.lastLocalRow == Some(2))

        assert(table.getLocal(TableCellPosition.parse("A1").get).isEmpty)
        assert(table.getLocal(TableCellPosition.parse("B1").get).get == TableCell(IntExpression(323)))
        assert(table.getLocal(TableCellPosition.parse("C1").get).get == TableCell(IntExpression(22)))
        assert(table.getLocal(TableCellPosition.parse("D1").get).isEmpty)
        assert(table.getLocal(TableCellPosition.parse("A2").get).isEmpty)
        assert(table.getLocal(TableCellPosition.parse("B2").get).isEmpty)
        assert(table.getLocal(TableCellPosition.parse("C2").get).isEmpty)
        assert(table.getLocal(TableCellPosition.parse("D2").get).isEmpty)
        assert(table.getLocal(TableCellPosition.parse("E2").get).get == TableCell(IntExpression(500)))
        assert(table.getLocal(TableCellPosition.parse("A3").get).get == TableCell(IntExpression(123)))
        assert(table.getLocal(TableCellPosition.parse("B3").get).isEmpty)
        assert(table.getLocal(TableCellPosition.parse("C3").get).get == TableCell(IntExpression(33)))
        assert(table.getLocal(TableCellPosition.parse("D3").get).isEmpty)
        assert(table.getLocal(TableCellPosition.parse("A4").get).isEmpty)
        assert(table.getLocal(TableCellPosition.parse("B4").get).isEmpty)
        assert(table.getLocal(TableCellPosition.parse("C4").get).isEmpty)
        assert(table.getLocal(TableCellPosition.parse("D4").get).isEmpty)
    }
}
