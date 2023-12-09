
package cliOpts.optRegister

import filters.ValueFilter
import table.TableCellPosition
import filters.Comparator
import expression.Expression
import expression.ConstantExpression
import expression.IntExpression
import expression.FloatExpression
import expression.StringExpression

class ValueFilterOptRegister(load: ValueFilter => Unit) extends DelegatingOptRegister[ValueFilter](load) {
    override def hasValue: Boolean = false
    override def isDefined: Boolean = false
    override def getOptional: Option[ValueFilter] = {
        throw new NotImplementedError()
    }
    override protected def getValueToLoad(it: Iterator[String]): ValueFilter = {
        val columnS = it.next().toUpperCase()
        val comparatorS = it.next()
        val valueS = it.next()

        if (columnS.exists(c => !c.isLetter)) {
            throw new Exception() // TODO: more specific exception
        }

        val columnIndex = TableCellPosition.getColumnIndex(columnS)
        val comparator = Comparator.parse(comparatorS).getOrElse {
            throw new Exception() // TODO: more specific exception
        }

        val value: ConstantExpression = valueS.toIntOption match {
            case Some(v) => IntExpression(v)
            case None => valueS.toFloatOption match {
                case Some(v) => FloatExpression(v)
                case None => StringExpression(valueS)
            }
        }

        return new ValueFilter(columnIndex, comparator, value)
    }
}
