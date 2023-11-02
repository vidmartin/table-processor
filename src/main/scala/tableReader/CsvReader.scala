
package tableReader

import scala.io.Source
import scala.util.matching.Regex

final case class CsvConfig(
    cellSeparator: String,
)

class CsvReader(input: Source, config: CsvConfig) extends TableReader {
    val lines = input.getLines().filter(l => !l.forall(c => c.isWhitespace))

    override def hasNext: Boolean = lines.hasNext
    
    override def next(): List[String] = {
        val line = lines.next()
        
        // TODO: CSV escaping

        line.split(Regex.quote(config.cellSeparator)).toList
    }
}
