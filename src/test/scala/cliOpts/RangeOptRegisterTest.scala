
package cliOpts

import org.scalatest.FunSuite
import cliOpts.optRegister.RangeOptRegister
import table.TableCellRange
import table.TableCellPosition

class RangeOptRegisterTest extends FunSuite {
    test("test1 (correct range)") {
        val reg = new RangeOptRegister()
        reg.load(List("A2", "E5").iterator)
        assert(reg.get == TableCellRange(
            TableCellPosition(1, 0),
            TableCellPosition(4, 4)
        ))
    }

    test("test2 (incorrect range)") {
        val reg = new RangeOptRegister()
        assertThrows[Exception] {
            // TODO: more specific exception
            reg.load(List("E5", "A2").iterator)
        }
    }
}
