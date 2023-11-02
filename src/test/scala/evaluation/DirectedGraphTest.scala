
package evaluation

import org.scalatest.FunSuite

class DirectedGraphTest extends FunSuite {
    test("test1") {
        var g = NeighborsDirectedGraph.edgeless(Iterable.range(0, 10))
        assert(!g.hasEdges)
        assert(g.nodes.size == 10)
        
        g = g.withEdge(4, 8)
        g = g.withEdge(4, 9)
        g = g.withEdge(3, 9)
        g = g.withEdge(1, 3)
        g = g.withEdge(1, 2)
        assert(g.hasEdges)

        assert(g.getChildren(1) == Set(2, 3))
        assert(g.getChildren(2) == Set.empty)
        assert(g.getChildren(3) == Set(9))
        assert(g.getChildren(4) == Set(8, 9))
        for (i <- Iterable.range(5, 10)) {
            assert(g.getChildren(i) == Set.empty)
        }

        assert(g.getParents(1) == Set.empty)
        assert(g.getParents(2) == Set(1))
        assert(g.getParents(3) == Set(1))
        for (i <- Iterable.range(4, 8)) {
            assert(g.getParents(i) == Set.empty)
        }
        assert(g.getParents(8) == Set(4))
        assert(g.getParents(9) == Set(3, 4))

        g = g.withoutEdge(1, 2)
        g = g.withoutEdge(1, 3)
        g = g.withoutEdge(1, 4)
        assert(g.getChildren(1) == Set.empty)
        assert(g.hasEdges)

        assert(g.isEdge(3, 9))
        g = g.withoutEdge(3, 9)
        assert(!g.isEdge(3, 9))

        g = g.withoutEdge(4, 8)
        g = g.withoutEdge(4, 9)
        assert(!g.hasEdges)
    }

    test("test2") {
        var children = Map(
            "zivocich" -> Set("savec", "plaz", "ptak"),
            "plaz" -> Set("zelva", "mlok"),
            "ptak" -> Set("orel", "holub"),
            "savec" -> Set("pes", "kocka", "clovek"),
            "pes" -> Set("bigl", "jezevcik"),
        )
        var g = NeighborsDirectedGraph.buildFromParents(
            List.from(children.values).appended(children.keySet).reduce((a, b) => a.union(b)),
            (v: String) => children.get(v).getOrElse(Set.empty),
        )
        
        for ((key, set) <- children) {
            assert(g.getParents(key) == set)
        }

        g = g.transposed
        for ((key, set) <- children) {
            assert(g.getChildren(key) == set)
        }
    }
}
