
package table

import org.scalatest.FunSuite
import tableReader.WrapperTableReader
import expression.IntExpression

class OffsetTableViewTest extends FunSuite {
    test("test1 - no offset") {
        val table = Table.parse(new WrapperTableReader(
            List(
                List("", "323", "22"),
                List("", "", "", "", "500"),
                List("123", "", "33"),
            ).iterator
        ))

        val offset = new OffsetTableView(0, 0, table)

        assert(offset.lastColumn == Some(4))
        assert(offset.lastRow == Some(2))

        assert(offset.get(TableCellPosition.parse("A1").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B1").get).get == TableCell(IntExpression(323)))
        assert(offset.get(TableCellPosition.parse("C1").get).get == TableCell(IntExpression(22)))
        assert(offset.get(TableCellPosition.parse("D1").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("A2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("C2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("D2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("E2").get).get == TableCell(IntExpression(500)))
        assert(offset.get(TableCellPosition.parse("A3").get).get == TableCell(IntExpression(123)))
        assert(offset.get(TableCellPosition.parse("B3").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("C3").get).get == TableCell(IntExpression(33)))
        assert(offset.get(TableCellPosition.parse("D3").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("A4").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B4").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("C4").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("D4").get).isEmpty)
    }

    test("test2 - offset by one row") {
        val table = Table.parse(new WrapperTableReader(
            List(
                List("", "323", "22"),
                List("", "", "", "", "500"),
                List("123", "", "33"),
            ).iterator
        ))

        val offset = new OffsetTableView(1, 0, table)

        assert(offset.lastColumn == Some(4))
        assert(offset.lastRow == Some(1))

        assert(offset.get(TableCellPosition.parse("A1").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B1").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("C1").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("D1").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("E1").get).get == TableCell(IntExpression(500)))
        assert(offset.get(TableCellPosition.parse("A2").get).get == TableCell(IntExpression(123)))
        assert(offset.get(TableCellPosition.parse("B2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("C2").get).get == TableCell(IntExpression(33)))
        assert(offset.get(TableCellPosition.parse("D2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("A3").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B3").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("C3").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("D3").get).isEmpty)
    }

    test("test3 - offset by one column") {
        val table = Table.parse(new WrapperTableReader(
            List(
                List("", "323", "22"),
                List("", "", "", "", "500"),
                List("123", "", "33"),
            ).iterator
        ))

        val offset = new OffsetTableView(0, 1, table)

        assert(offset.lastColumn == Some(3))
        assert(offset.lastRow == Some(2))

        assert(offset.get(TableCellPosition.parse("A1").get).get == TableCell(IntExpression(323)))
        assert(offset.get(TableCellPosition.parse("B1").get).get == TableCell(IntExpression(22)))
        assert(offset.get(TableCellPosition.parse("C1").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("A2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("C2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("D2").get).get == TableCell(IntExpression(500)))
        assert(offset.get(TableCellPosition.parse("A3").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B3").get).get == TableCell(IntExpression(33)))
        assert(offset.get(TableCellPosition.parse("C3").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("A4").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B4").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("C4").get).isEmpty)
    }

    test("test4 - offset by 1 row and 1 column") {
        val table = Table.parse(new WrapperTableReader(
            List(
                List("", "323", "22"),
                List("", "", "", "", "500"),
                List("123", "", "33"),
            ).iterator
        ))

        val offset = new OffsetTableView(1, 1, table)

        assert(offset.lastColumn == Some(3))
        assert(offset.lastRow == Some(1))

        assert(offset.get(TableCellPosition.parse("A1").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B1").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("C1").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("D1").get).get == TableCell(IntExpression(500)))
        assert(offset.get(TableCellPosition.parse("A2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B2").get).get == TableCell(IntExpression(33)))
        assert(offset.get(TableCellPosition.parse("C2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("A3").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B3").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("C3").get).isEmpty)
    }

    test("test5 - offset by 2 rows and 2 columns") {
        val table = Table.parse(new WrapperTableReader(
            List(
                List("", "323", "22"),
                List("", "", "", "", "500"),
                List("123", "", "33"),
            ).iterator
        ))

        val offset = new OffsetTableView(2, 2, table)

        assert(offset.lastColumn == Some(2))
        assert(offset.lastRow == Some(0))

        assert(offset.get(TableCellPosition.parse("A1").get).get == TableCell(IntExpression(33)))
        assert(offset.get(TableCellPosition.parse("B1").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("A2").get).isEmpty)
        assert(offset.get(TableCellPosition.parse("B2").get).isEmpty)
    }
}
