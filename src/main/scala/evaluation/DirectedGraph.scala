
package evaluation

abstract class DirectedGraph[T] {
    def nodes: collection.Set[T]
    def getChildren(node: T): collection.Set[T]
    def getParents(node: T): collection.Set[T]
    def isEdge(from: T, to: T): Boolean
    def withEdge(from: T, to: T): DirectedGraph[T]
    def withoutEdge(from: T, to: T): DirectedGraph[T]
    def transposed: DirectedGraph[T]
}
