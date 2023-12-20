
package tableReader

/** reads rows of some table */
abstract class TableReader extends Iterator[List[String]] {
    override def hasNext: Boolean
    override def next(): List[String]
}
