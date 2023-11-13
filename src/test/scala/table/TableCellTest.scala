
package table

import org.scalatest.FunSuite

class TableCellTest extends FunSuite {
    test("test1") {
        assert(TableCell.parse("assdjjjkhj") == Some(ValueTableCell(StringTableCellValue("assdjjjkhj"))))
        assert(TableCell.parse("@") == Some(ValueTableCell(StringTableCellValue("@"))))
        assert(TableCell.parse("231.123") == Some(ValueTableCell(FloatTableCellValue(231.123f))))
        assert(TableCell.parse("231,123") == Some(ValueTableCell(StringTableCellValue("231,123"))))
    }

    test("test2") {
        assert(TableCell.parse("").isEmpty)
        assert(TableCell.parse("    ").isEmpty)
        assert(TableCell.parse("1234") == Some(ValueTableCell(IntegerTableCellValue(1234))))
        assert(TableCell.parse("=53*A1") == Some(FormulaTableCell("53*A1")))
    }
}