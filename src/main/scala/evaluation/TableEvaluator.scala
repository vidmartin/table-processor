
package evaluation

import scala.collection.mutable.{HashMap => HashMapMut}
import scala.collection.immutable.HashMap
import scala.collection.immutable.HashSet
import table._

object TableEvaluator extends BaseTableEvaluator {
    override def evaluateTable(table: Table[TableCell]): Table[ValueTableCell] = {
        val cache = new HashMapMut[TableCellPosition, ValueTableCell]

        for (pos <- table.nonEmpty()) {
            evaluateCell(table, cache, pos, HashSet())
        }
        
        new Table[ValueTableCell](HashMap.from(cache))
    }

    private def evaluateCell(
        table: Table[TableCell],
        cache: HashMapMut[TableCellPosition, ValueTableCell],
        pos: TableCellPosition,
        refset: HashSet[TableCellPosition]
    ): Unit = {
        if (cache.contains(pos)) {
            return
        }

        if (refset.contains(pos)) {
            throw new Exception() // TODO: more specific exception (circular dependency between cells)
        }

        cache.put(
            pos,
            ValueTableCell(
                table.get(pos).evaluate(
                    pos2 => {
                        evaluateCell(table, cache, pos2, refset + pos2)
                        cache.get(pos2).get.value
                    }
                )
            )
        )
    }
}