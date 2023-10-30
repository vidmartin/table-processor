
package table

import org.scalatest.FunSuite
import tableReader.TableReader
import tableReader.WrapperTableReader

class TableTest extends FunSuite {
    test("test1") {
        val table = Table.parse(new WrapperTableReader(
            List(
                List("", "323", "22"),
                List("", "", "", "", "500"),
                List("123", "", "33"),
            ).iterator
        ))

        assert(table.get(TableCellPosition.parse("A1").get) == ValueTableCell(EmptyTableCellValue))
        assert(table.get(TableCellPosition.parse("B1").get) == ValueTableCell(IntegerTableCellValue(323)))
        assert(table.get(TableCellPosition.parse("C1").get) == ValueTableCell(IntegerTableCellValue(22)))
        assert(table.get(TableCellPosition.parse("D1").get) == ValueTableCell(EmptyTableCellValue))
        assert(table.get(TableCellPosition.parse("A2").get) == ValueTableCell(EmptyTableCellValue))
        assert(table.get(TableCellPosition.parse("B2").get) == ValueTableCell(EmptyTableCellValue))
        assert(table.get(TableCellPosition.parse("C2").get) == ValueTableCell(EmptyTableCellValue))
        assert(table.get(TableCellPosition.parse("D2").get) == ValueTableCell(EmptyTableCellValue))
        assert(table.get(TableCellPosition.parse("E2").get) == ValueTableCell(IntegerTableCellValue(500)))
        assert(table.get(TableCellPosition.parse("A3").get) == ValueTableCell(IntegerTableCellValue(123)))
        assert(table.get(TableCellPosition.parse("B3").get) == ValueTableCell(EmptyTableCellValue))
        assert(table.get(TableCellPosition.parse("C3").get) == ValueTableCell(IntegerTableCellValue(33)))
        assert(table.get(TableCellPosition.parse("D3").get) == ValueTableCell(EmptyTableCellValue))
        assert(table.get(TableCellPosition.parse("A4").get) == ValueTableCell(EmptyTableCellValue))
        assert(table.get(TableCellPosition.parse("B4").get) == ValueTableCell(EmptyTableCellValue))
        assert(table.get(TableCellPosition.parse("C4").get) == ValueTableCell(EmptyTableCellValue))
        assert(table.get(TableCellPosition.parse("D4").get) == ValueTableCell(EmptyTableCellValue))
    }
}
