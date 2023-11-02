
package table

import scala.util.matching.Regex

final case class TableCellPosition(row: Int, column: Int)

object TableCellPosition {
    val LETTER_COUNT = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".length()
    val FIRST_LETTER_INDEX = 'A'.toInt

    def parse(s: String): Option[TableCellPosition] = {
        """^([A-Z]+)(\d+)$""".r.findFirstMatchIn(s).map(
            m => new TableCellPosition(
                m.group(2).toInt - 1,
                getColIndex(m.group(1))
            )
        ).filter(
            pos => (pos.column >= 0) && (pos.row >= 0)
        )
    }

    private def getColIndex(col: String): Int = {
        // assertion: all chars in col are uppercase letters
        var index: Int = 0
        for (c <- col) {
            index = LETTER_COUNT * index + (c.toInt - FIRST_LETTER_INDEX)
        }
        index
    }
}
