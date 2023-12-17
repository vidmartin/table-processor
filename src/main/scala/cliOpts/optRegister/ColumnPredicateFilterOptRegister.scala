
package cliOpts.optRegister

import table.TableCellPosition
import cliOpts.FormatArgException

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
