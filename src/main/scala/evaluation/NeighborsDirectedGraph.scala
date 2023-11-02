
package evaluation

class NeighborsDirectedGraph[T] private(
    override val nodes: collection.Set[T],
    childrenMap: Map[T, collection.Set[T]],
    parentsMap: Map[T, collection.Set[T]],
) extends DirectedGraph[T] {
    // invariant: childrenMap & parentsMap dont contain empty sets as values!!!

    private def assertNodeKnown(node: T) {
        if (!nodes.contains(node)) {
            throw new Exception() // TODO: more specific exception
        }
    }

    for ((node, children) <- childrenMap) {
        assertNodeKnown(node)
        for (child <- children) {
            assertNodeKnown(child)
        }
    }
    for ((node, parents) <- parentsMap) {
        assertNodeKnown(node)
        for (parent <- parents) {
            assertNodeKnown(parent)
        }
    }
    
    override def getChildren(node: T): collection.Set[T] = childrenMap.get(node).getOrElse(Set.empty)
    override def getParents(node: T): collection.Set[T] = parentsMap.get(node).getOrElse(Set.empty)
    override def isEdge(from: T, to: T): Boolean = {
        assertNodeKnown(from)
        assertNodeKnown(to)
        return childrenMap.get(from).get.contains(to)
    }
    override def withEdge(from: T, to: T): NeighborsDirectedGraph[T] = {
        assertNodeKnown(from)
        assertNodeKnown(to)
        return new NeighborsDirectedGraph[T](
            nodes,
            childrenMap.updatedWith(from) { set => Some(set.getOrElse(Set.empty) + to) },
            parentsMap.updatedWith(to) { set => Some(set.getOrElse(Set.empty) + from) },
        )
    }
    override def withoutEdge(from: T, to: T): NeighborsDirectedGraph[T] = {
        assertNodeKnown(from)
        assertNodeKnown(to)
        return new NeighborsDirectedGraph[T](
            nodes,
            childrenMap.updatedWith(from) { set => Some(set.get - to).filter(_.nonEmpty) },
            parentsMap.updatedWith(to) { set => Some(set.get - from).filter(_.nonEmpty) },
        )
    }
    
    def transposed: NeighborsDirectedGraph[T] = new NeighborsDirectedGraph[T](
        nodes, parentsMap, childrenMap
    )
}

object NeighborsDirectedGraph {
    def edgeless[T](nodes: Iterable[T]): NeighborsDirectedGraph[T] = new NeighborsDirectedGraph[T](
        Set.from(nodes), Map.empty, Map.empty
    )

    def buildFromParents[T](nodes: Iterable[T], parents: T => Iterable[T]): NeighborsDirectedGraph[T] = {
        var graph = edgeless(nodes)
        for (from <- nodes) {
            for (to <- parents(from)) {
                graph = graph.withEdge(from, to)
            }
        }
        return graph
    }
}
