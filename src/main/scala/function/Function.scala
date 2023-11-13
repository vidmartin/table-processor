
package function

import table.TableCellValue
import table.IntegerTableCellValue
import table.FloatTableCellValue
import table.StringTableCellValue
import scala.collection.StepperShape

abstract class Function {
    def paramCount: Int
    def evaluate(params: Array[FunctionParam]): FunctionParam = {
        if (params.length != paramCount) {
            throw new Exception() // TODO: more specific exception
        }
        return evaluate(params)
    }
    protected def evaluateInner(params: Array[FunctionParam]): FunctionParam
}

abstract class BinaryFunction extends Function {
    override def paramCount: Int = 2
    override def evaluateInner(params: Array[FunctionParam]): FunctionParam = {
        (params(0), params(1)) match {
            case (FunctionParamValue(lhs), FunctionParamValue(rhs)) => FunctionParamValue(evaluate(lhs, rhs))
            case _ => throw new Exception() // TODO: more specific exception
        }
    }

    def evaluate(lhsWrap: TableCellValue, rhsWrap: TableCellValue): TableCellValue
}

abstract class AggregateFunction extends Function {
    override def paramCount: Int = 1
    override def evaluateInner(params: Array[FunctionParam]): FunctionParam = {
        (params(0)) match {
            case (FunctionParamRange(width, array)) => FunctionParamValue(evaluate(width, array))
            case _ => throw new Exception() // TODO: more specific exception
        }
    }

    def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue
}

abstract class RowAggregateFunction extends Function {
    override def paramCount: Int = 1
    override def evaluateInner(params: Array[FunctionParam]): FunctionParam = {
        (params(0)) match {
            case (FunctionParamRange(width, array)) => FunctionParamRange(
                1, array.grouped(width).map(row => evaluateRow(row)).toArray
            )
            case _ => throw new Exception() // TODO: more specific exception
        }
    }

    def evaluateRow(array: Array[TableCellValue]): TableCellValue
}

abstract class ColumnAggregateFunction extends Function {
    override def paramCount: Int = 1
    override def evaluateInner(params: Array[FunctionParam]): FunctionParam = {
        (params(0)) match {
            case (FunctionParamRange(width, array)) => FunctionParamRange(
                width, Iterable.range(0, width).map(
                    i => evaluateColumn(
                        Iterable.range(i, array.length, width).map(j => array(j)).toArray
                    )
                ).toArray
            )
            case _ => throw new Exception() // TODO: more specific exception
        }
    }

    def evaluateColumn(array: Array[TableCellValue]): TableCellValue
}

object Function {
    object ADD extends BinaryFunction {
        override def evaluate(lhsWrap: TableCellValue, rhsWrap: TableCellValue): TableCellValue = {
            (lhsWrap, rhsWrap) match {
                case (IntegerTableCellValue(a), IntegerTableCellValue(b)) => IntegerTableCellValue(a + b)
                case (StringTableCellValue(lhs), StringTableCellValue(rhs)) => StringTableCellValue(lhs + rhs)
                case _ => (lhsWrap.getFloat, rhsWrap.getFloat) match {
                    case (Some(a), Some(b)) => FloatTableCellValue(a + b)
                    case _ => throw new Exception() // TODO: more specific
                }
            }
        }
    }

    object SUB extends BinaryFunction {
        override def evaluate(lhsWrap: TableCellValue, rhsWrap: TableCellValue): TableCellValue = {
            (lhsWrap, rhsWrap) match {
                case (IntegerTableCellValue(a), IntegerTableCellValue(b)) => IntegerTableCellValue(a - b)
                case _ => (lhsWrap.getFloat, rhsWrap.getFloat) match {
                    case (Some(a), Some(b)) => FloatTableCellValue(a + b)
                    case _ => throw new Exception() // TODO: more specific
                }
            }
        }
    }

    object MUL extends BinaryFunction {
        override def evaluate(lhsWrap: TableCellValue, rhsWrap: TableCellValue): TableCellValue = {
            (lhsWrap, rhsWrap) match {
                case (IntegerTableCellValue(a), IntegerTableCellValue(b)) => IntegerTableCellValue(a * b)
                case (IntegerTableCellValue(i), StringTableCellValue(s)) => StringTableCellValue(s.repeat(i))
                case (StringTableCellValue(s), IntegerTableCellValue(i)) => StringTableCellValue(s.repeat(i))
                case _ => (lhsWrap.getFloat, rhsWrap.getFloat) match {
                    case (Some(a), Some(b)) => FloatTableCellValue(a * b)
                    case _ => throw new Exception() // TODO: more specific
                }
            }
        }
    }

    object DIV extends BinaryFunction {
        override def evaluate(lhsWrap: TableCellValue, rhsWrap: TableCellValue): TableCellValue = {
            (lhsWrap, rhsWrap) match {
                case (IntegerTableCellValue(a), IntegerTableCellValue(b)) => {
                    if (a % b == 0) {
                        IntegerTableCellValue(a / b)
                    } else {
                        FloatTableCellValue(a.floatValue() / b.floatValue())
                    }
                }
                case _ => (lhsWrap.getFloat, rhsWrap.getFloat) match {
                    case (Some(a), Some(b)) => FloatTableCellValue(a / b)
                    case _ => throw new Exception() // TODO: more specific
                }
            }
        }
    }

    object MOD extends BinaryFunction {
        override def evaluate(lhsWrap: TableCellValue, rhsWrap: TableCellValue): TableCellValue = {
            (lhsWrap, rhsWrap) match {
                case (IntegerTableCellValue(a), IntegerTableCellValue(b)) => IntegerTableCellValue(a % b)
                case _ => (lhsWrap.getFloat, rhsWrap.getFloat) match {
                    case (Some(a), Some(b)) => FloatTableCellValue(a % b)
                    case _ => throw new Exception() // TODO: more specific
                }
            }
        }
    }

    object MAX2 extends BinaryFunction {
        override def evaluate(lhsWrap: TableCellValue, rhsWrap: TableCellValue): TableCellValue = {
            (lhsWrap, rhsWrap) match {
                case (IntegerTableCellValue(a), IntegerTableCellValue(b)) => IntegerTableCellValue(a.max(b))
                case _ => (lhsWrap.getFloat, rhsWrap.getFloat) match {
                    case (Some(a), Some(b)) => FloatTableCellValue(a.max(b))
                    case _ => throw new Exception() // TODO: more specific
                }
            }
        }
    }

    object MIN2 extends BinaryFunction {
        override def evaluate(lhsWrap: TableCellValue, rhsWrap: TableCellValue): TableCellValue = {
            (lhsWrap, rhsWrap) match {
                case (IntegerTableCellValue(a), IntegerTableCellValue(b)) => IntegerTableCellValue(a.min(b))
                case _ => (lhsWrap.getFloat, rhsWrap.getFloat) match {
                    case (Some(a), Some(b)) => FloatTableCellValue(a.min(b))
                    case _ => throw new Exception() // TODO: more specific
                }
            }
        }
    }

    object SUM extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            array.reduce((a, b) => ADD.evaluate(a, b))
        }
    }

    object AVG extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            DIV.evaluate(SUM.evaluate(width, array), IntegerTableCellValue(array.length))
        }
    }

    object MAX extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            array.reduce((a, b) => MAX2.evaluate(a, b))
        }
    }

    object MIN extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            array.reduce((a, b) => MIN2.evaluate(a, b))
        }
    }

    object RSUM extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            array.
            array.reduce((a, b) => ADD.evaluate(a, b))
        }
    }

    object RAVG extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            DIV.evaluate(SUM.evaluate(width, array), IntegerTableCellValue(array.length))
        }
    }

    object RMAX extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            array.reduce((a, b) => MAX2.evaluate(a, b))
        }
    }

    object RMIN extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            array.reduce((a, b) => MIN2.evaluate(a, b))
        }
    }

    object CSUM extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            array.reduce((a, b) => ADD.evaluate(a, b))
        }
    }

    object CAVG extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            DIV.evaluate(SUM.evaluate(width, array), IntegerTableCellValue(array.length))
        }
    }

    object CMAX extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            array.reduce((a, b) => MAX2.evaluate(a, b))
        }
    }

    object CMIN extends AggregateFunction {
        override def evaluate(width: Int, array: Array[TableCellValue]): TableCellValue = {
            array.reduce((a, b) => MIN2.evaluate(a, b))
        }
    }

    val namedFunctions: Map[String, Function] = ???
}
