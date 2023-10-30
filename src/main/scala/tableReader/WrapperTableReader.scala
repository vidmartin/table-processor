
package tableReader

class WrapperTableReader(it: Iterator[List[String]]) extends TableReader {
    override def hasNext: Boolean = it.hasNext
    override def next(): List[String] = it.next()
}
