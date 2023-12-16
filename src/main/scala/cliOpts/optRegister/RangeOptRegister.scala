
package cliOpts.optRegister

import table.TableCellRange
import table.TableCellPosition

class RangeOptRegister extends OptRegister[TableCellRange] {
    private var value: Option[TableCellRange] = None
    override protected def forceLoad(it: Iterator[String]): Unit = {
        value = Some(
            TableCellRange(
                TableCellPosition.parse(it.next()).get,
                TableCellPosition.parse(it.next()).get,
            )
        )
    }
    override def hasValue: Boolean = getOptional.nonEmpty
    override def isDefined: Boolean = getOptional.nonEmpty
    override def getOptional: Option[TableCellRange] = value
    override def hasArgs: Boolean = true
    override def accept[T](visitor: OptRegisterVisitor[T]): T = {
        visitor.visitRangeOptRegister(this)
    }
}
