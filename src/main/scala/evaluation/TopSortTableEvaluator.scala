
package evaluation

import table._
import java.util.Collection
import scala.collection.immutable.HashMap
import scala.collection.mutable.{HashMap => HashMapMut}

class TopSortTableEvaluator extends BaseTableEvaluator {
    override def evaluateTable(srcTable: Table[TableCell]): Table[ValueTableCell] = {
        val graph = NeighborsDirectedGraph.buildFromParents[TableCellPosition](
            srcTable.nonEmptyPositions,
            pos => srcTable.get(pos).get.dependsOn
        )
        val sorted = TopSorter.topSort(graph)
        var wipTable = Table.empty[ValueTableCell]
        for (posToEvaluate <- sorted) {
            wipTable = wipTable.withSet(
                posToEvaluate,
                new ValueTableCell(
                    srcTable.get(posToEvaluate).get.evaluate(
                        referencedPos => wipTable.get(referencedPos).get.value
                    )
                )
            )
        }
        ???
    }
}
