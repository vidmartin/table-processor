
package table

import org.scalatest.FunSuite

class TableCellPositionTest extends FunSuite {
    test("test1") {
        assert(TableCellPosition.parse("").isEmpty)
        assert(TableCellPosition.parse("asdadssa").isEmpty)
        assert(TableCellPosition.parse("012").isEmpty)
        assert(TableCellPosition.parse("4B").isEmpty)
    }

    test("test2") {
        assert(TableCellPosition.parse("B13") == Some(TableCellPosition(12, 1)))
        assert(TableCellPosition.parse("D20") == Some(TableCellPosition(19, 3)))
        assert(TableCellPosition.parse("A0") == None)
        assert(TableCellPosition.parse("Z100") == Some(TableCellPosition(99, 25)))
        assert(TableCellPosition.parse("A100") == Some(TableCellPosition(99, 0)))
        assert(TableCellPosition.parse("FF555") == Some(TableCellPosition(554, 135)))
    }
}
