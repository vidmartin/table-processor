
package table

import scala.util.matching.Regex

final case class TableCellPosition(row: Int, column: Int) {
    override def toString(): String = {
        f"${TableCellPosition.getColumnName(column)}${TableCellPosition.getRowName(row)}"
    }
}

object TableCellPosition {
    val LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val FIRST_LETTER_INDEX = 'A'.toInt

    def parse(s: String): Option[TableCellPosition] = {
        """^([A-Z]+)(\d+)$""".r.findFirstMatchIn(s).map(
            m => new TableCellPosition(
                m.group(2).toInt - 1,
                getColumnIndex(m.group(1))
            )
        ).filter(
            pos => (pos.column >= 0) && (pos.row >= 0)
        )
    }

    private def getColumnIndex(columnName: String): Int = {
        // assertion: all chars in col are uppercase letters
        var index: Int = 0
        for (c <- columnName) {
            index = LETTERS.length() * index + (c.toInt - FIRST_LETTER_INDEX)
        }
        index
    }

    def getColumnName(columnIndex: Int): String = {
        var ret = ""
        var temp = columnIndex + 1
        while (temp > 0) {
            ret = ret + LETTERS(columnIndex % LETTERS.length())
            temp /= LETTERS.length()
        }
        return ret
    }

    def getRowName(rowIndex: Int): String = (rowIndex + 1).toString()
}
