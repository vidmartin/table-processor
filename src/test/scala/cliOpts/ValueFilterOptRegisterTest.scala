
package cliOpts

import org.scalatest.FunSuite
import cliOpts.optRegister.ValueFilterOptRegister
import scala.collection.mutable.ArrayBuffer
import filters.RowFilter
import expression.ConstantExpression
import filters.ValueFilter
import filters.Comparator
import expression.IntExpression
import expression.StringExpression

class ValueFilterOptRegisterTest extends FunSuite {
    test("test1") {
        val list = new ArrayBuffer[RowFilter[ConstantExpression]]
        val reg = new ValueFilterOptRegister(list.addOne(_))
        reg.load(List("A", "<", "7").iterator)
        assert(list.toList == List(
            ValueFilter(2, Comparator.LT, IntExpression(7))
        ))
    }

    test("test2") {
        val list = new ArrayBuffer[RowFilter[ConstantExpression]]
        val reg = new ValueFilterOptRegister(list.addOne(_))
        reg.load(List("C", "==", "aaaabbbb").iterator)
        reg.load(List("FG", "!=", "bbbbaaaa").iterator)
        assert(list.toList == List(
            ValueFilter(2, Comparator.EQ, StringExpression("aaaabbbb")),
            ValueFilter(162, Comparator.NE, StringExpression("bbbbaaaa"))
        ))
    }
}
