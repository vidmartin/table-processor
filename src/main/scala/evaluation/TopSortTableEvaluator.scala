
package evaluation

import table._
import java.util.Collection
import scala.collection.immutable.HashMap

class TopSortTableEvaluator extends BaseTableEvaluator {
    override def evaluateTable(table: Table[TableCell]): Table[ValueTableCell] = {
        // val graph = NeighborsDirectedGraph.buildFromParents[TableCellPosition](
        //     table.nonEmptyPositions,
        //     pos => table.get(pos).dependsOn
        // )
        // val sorted = TopSorter.topSort()
        ???
    }
}
