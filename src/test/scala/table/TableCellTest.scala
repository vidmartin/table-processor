
package table

import org.scalatest.FunSuite

class TableCellTest extends FunSuite {
    test("test1") {
        // TODO: more specific exceptions
        assertThrows[Exception] { TableCell.parse("assdjjjkhj").isEmpty }
        assertThrows[Exception] { TableCell.parse("@").isEmpty }
        assertThrows[Exception] { TableCell.parse("231.123").isEmpty }
        assertThrows[Exception] { TableCell.parse("231,123").isEmpty }
    }

    test("test2") {
        assert(TableCell.parse("").isEmpty)
        assert(TableCell.parse("    ").isEmpty)
        assert(TableCell.parse("1234") == Some(ValueTableCell(IntegerTableCellValue(1234))))
        assert(TableCell.parse("=53*A1") == Some(FormulaTableCell("53*A1")))
    }
}