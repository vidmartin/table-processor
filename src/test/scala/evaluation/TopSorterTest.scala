
package evaluation

import org.scalatest.FunSuite

class TopSorterTest extends FunSuite {
    test("test1") {
        val g = NeighborsDirectedGraph.buildFromParents(
            Iterable.range(1, 13),
            (v: Int) => Iterable.range(1, v).filter(d => v % d == 0)
        )

        var it = TopSorter.topSort(g).iterator
        assert(it.take(1).toSet == Set(1)) // no divisors
        assert(it.take(5).toSet == Set(2, 3, 5, 7, 11)) // only divisor is 1
        assert(it.take(4).toSet == Set(4, 6, 9, 10)) // only divisors are 1 and primes
        assert(it.take(2).toSet == Set(8, 12))
    }
}
