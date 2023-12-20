
package cliOpts.optRegister

import table.TableCellPosition
import cliOpts.FormatArgException

/** Loads a name of a column and converts it to an integer. Instead of storing it, passes it to the function passed via constructor param. */
class ColumnPredicateFilterOptRegister(load: Int => Unit) extends DelegatingOptRegister[Int](load) {
    override def getOptional: Option[Int] = {
        throw new NotImplementedError()
    }
    override protected def getValueToLoad(it: Iterator[String]): Int = {
        val columnS = it.next().toUpperCase()

        if (columnS.exists(c => !c.isLetter)) {
            throw new FormatArgException(f"invalid column specification: '${columnS}'")
        }

        TableCellPosition.getColumnIndex(columnS)
    }
    override def hasArgs: Boolean = true
    
    override def accept[T](visitor: OptRegisterVisitor[T]): T = {
        visitor.visitColumnPredicateFilterOptRegister(this)
    }
}
