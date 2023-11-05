
package function

import table.TableCellValue
import table.IntegerTableCellValue
import table.FloatTableCellValue
import table.StringTableCellValue

abstract class Function {
    def paramCount: Int
    def evaluate(params: Array[FunctionParam]): TableCellValue = {
        if (params.length != paramCount) {
            throw new Exception() // TODO: more specific exception
        }
        return evaluateInner(params)
    }
    protected def evaluateInner(params: Array[FunctionParam]): TableCellValue
}

abstract class BinaryFunctionWithParamsOfTheSameType extends Function {
    override def paramCount: Int = 2
    override protected def evaluateInner(params: Array[FunctionParam]): TableCellValue = {
        val (lhsWrap, rhsWrap) = (params(0), params(1)) match {
            case (FunctionParamValue(lhs), FunctionParamValue(rhs)) => (lhs, rhs)
            case _ => throw new Exception() // TODO: more specific exception
        }
        (lhsWrap, rhsWrap) match {
            case (IntegerTableCellValue(lhs), IntegerTableCellValue(rhs)) => IntegerTableCellValue(evaluateInts(lhs, rhs))
            case (FloatTableCellValue(lhs), FloatTableCellValue(rhs)) => FloatTableCellValue(evaluateFloats(lhs, rhs))
            case (StringTableCellValue(lhs), StringTableCellValue(rhs)) => StringTableCellValue(evaluateStrings(lhs, rhs))
            case _ => throw new Exception() // TODO: more specific exception
        }
    }

    protected def evaluateInts(lhs: Int, rhs: Int): Int
    protected def evaluateFloats(lhs: Float, rhs: Float): Float
    protected def evaluateStrings(lhs: String, rhs: String): String
}

object Function {
    object ADD extends BinaryFunctionWithParamsOfTheSameType {
        override protected def evaluateInts(lhs: Int, rhs: Int): Int = lhs + rhs
        override protected def evaluateFloats(lhs: Float, rhs: Float): Float = lhs + rhs
        override protected def evaluateStrings(lhs: String, rhs: String): String = lhs + rhs
    }

    object SUB extends BinaryFunctionWithParamsOfTheSameType {
        override protected def evaluateInts(lhs: Int, rhs: Int): Int = lhs - rhs
        override protected def evaluateFloats(lhs: Float, rhs: Float): Float = lhs - rhs
        override protected def evaluateStrings(lhs: String, rhs: String): String = throw new Exception() // TODO: more specific exception
    }

    object MUL extends BinaryFunctionWithParamsOfTheSameType {
        override protected def evaluateInts(lhs: Int, rhs: Int): Int = lhs * rhs
        override protected def evaluateFloats(lhs: Float, rhs: Float): Float = lhs * rhs
        override protected def evaluateStrings(lhs: String, rhs: String): String = throw new Exception() // TODO: more specific exception
    }

    object DIV extends BinaryFunctionWithParamsOfTheSameType {
        override protected def evaluateInts(lhs: Int, rhs: Int): Int = lhs / rhs
        override protected def evaluateFloats(lhs: Float, rhs: Float): Float = lhs / rhs
        override protected def evaluateStrings(lhs: String, rhs: String): String = throw new Exception() // TODO: more specific exception
    }

    object MOD extends BinaryFunctionWithParamsOfTheSameType {
        override protected def evaluateInts(lhs: Int, rhs: Int): Int = lhs % rhs
        override protected def evaluateFloats(lhs: Float, rhs: Float): Float = lhs % rhs
        override protected def evaluateStrings(lhs: String, rhs: String): String = throw new Exception() // TODO: more specific exception
    }

    val namedFunctions: Map[String, Function] = ???
}
