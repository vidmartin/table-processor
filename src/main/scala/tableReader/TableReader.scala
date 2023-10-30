
package tableReader

abstract class TableReader extends Iterator[List[String]] {
    override def hasNext: Boolean
    override def next(): List[String]
}
