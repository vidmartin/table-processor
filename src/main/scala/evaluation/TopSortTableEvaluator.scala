
package evaluation

import table._
import java.util.Collection
import scala.collection.immutable.HashMap
import scala.collection.mutable.{HashMap => HashMapMut}
import expression.Expression
import expression.ConstantExpression
import expression.ExpressionEvaluationContext
import expression.ExpressionException

/** an implementation of BaseTableEvaluator that uses top sort for resolving dependencies between cells */
object TopSortTableEvaluator extends BaseTableEvaluator {
    override def evaluateTable(srcTable: Table[Expression]): Table[ConstantExpression] = {
        val graph = NeighborsDirectedGraph.buildFromParents[TableCellPosition](
            srcTable.allRelevantLocalPositions,
            pos => srcTable.getLocal(pos).expr.referencedPositions
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

        var wipTable = Table.empty[ConstantExpression]

        val evaluationContext = new ExpressionEvaluationContext {
            def get(pos: TableCellPosition): Expression = {
                wipTable.getLocal(pos).expr
            }
        }

        for (posToEvaluate <- sorted) {
            try {
                wipTable = wipTable.withSet(
                    posToEvaluate,
                    new TableCell(
                        srcTable.getLocal(posToEvaluate).expr.evaluate(evaluationContext)
                    )
                )
            } catch {
                case e: ExpressionException => {
                    throw new CellExpressionEvaluationException(posToEvaluate, e)
                }
            }
        }
        return wipTable
    }
}
