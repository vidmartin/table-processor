
package evaluation

import table._
import java.util.Collection
import scala.collection.immutable.HashMap
import scala.collection.mutable.{HashMap => HashMapMut}

object TopSortTableEvaluator extends BaseTableEvaluator {
    override def evaluateTable(srcTable: Table[TableCell]): Table[ValueTableCell] = {
        val graph = NeighborsDirectedGraph.buildFromParents[TableCellPosition](
            srcTable.nonEmptyPositions,
            pos => srcTable.get(pos).get.dependsOn
        )

        val sorted = try { TopSorter.topSort(graph) } catch {
            case UnexpectedCycleException(message, prunedGraph) => {
                val typedPrunedGraph = prunedGraph.asInstanceOf[DirectedGraph[TableCellPosition]]
                val offenders = typedPrunedGraph.nodes.filter(
                    u => typedPrunedGraph.getChildren(u).nonEmpty
                )
                throw new CircularReferenceException(
                    f"the table contains circular references and therefore can't be evaluated; list of positions which are part of a cycle: ${offenders.mkString(", ")}"
                )
            }
        }

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
        return wipTable
    }
}
