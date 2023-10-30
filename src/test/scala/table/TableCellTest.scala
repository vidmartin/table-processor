
package table

import org.scalatest.FunSuite

class TableCellTest extends FunSuite {
    test("test1") {
        assert(TableCell.parse("assdjjjkhj").isEmpty)
        assert(TableCell.parse("@").isEmpty)
        assert(TableCell.parse("231.123").isEmpty)
        assert(TableCell.parse("231,123").isEmpty)
    }

    test("test2") {
        assert(TableCell.parse("") == Some(ValueTableCell(EmptyTableCellValue)))
        assert(TableCell.parse("    ") == Some(ValueTableCell(EmptyTableCellValue)))
        assert(TableCell.parse("1234") == Some(ValueTableCell(IntegerTableCellValue(1234))))
        assert(TableCell.parse("=53*A1") == Some(FormulaTableCell("53*A1")))
    }
}