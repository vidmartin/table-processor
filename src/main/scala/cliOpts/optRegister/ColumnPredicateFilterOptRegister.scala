
package cliOpts.optRegister

import table.TableCellPosition

class ColumnPredicateFilterOptRegister(load: Int => Unit) extends DelegatingOptRegister[Int](load) {
    override def getOptional: Option[Int] = {
        throw new NotImplementedError()
    }
    override protected def getValueToLoad(it: Iterator[String]): Int = {
        val columnS = it.next().toUpperCase()

        if (columnS.exists(c => !c.isLetter)) {
            throw new Exception() // TODO: more specific exception
        }

        TableCellPosition.getColumnIndex(columnS)
    }
}
