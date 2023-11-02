
package evaluation

import scala.collection.mutable.Queue

object TopSorter {
    def topSort[T](graph: DirectedGraph[T]): List[T] = {
        val queue = Queue.from(graph.nodes.filter(u => graph.getParents(u).isEmpty))
        var list: List[T] = List.empty
        var g = graph
        while (queue.nonEmpty) {
            val curr = queue.dequeue()
            list = list.appended(curr)
            for (child <- graph.getChildren(curr)) {
                g = g.withoutEdge(curr, child)
                if (g.getParents(child).isEmpty) {
                    queue.enqueue(child)
                }
            }
        }
        if (g.hasEdges) {
            throw new UnexpectedCycleException("cannot run TopSort on an acyclic graph", g)
        }
        return list
    }
}
