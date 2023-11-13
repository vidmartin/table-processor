
package table

abstract class TableCellValue {
    def getInt: Option[Int]
    def getFloat: Option[Float]
    def getString: Option[String]

    def getBool: Boolean = {
        getInt.map(_ != 0).orElse(
            getFloat.map(_ != 0)
        ).orElse(
            getString.map(!_.forall(c => c.isWhitespace))
        ).getOrElse(false)
    }
}

final case class IntegerTableCellValue(value: Int) extends TableCellValue {
    def getInt: Option[Int] = Some(value)
    def getFloat: Option[Float] = Some(value)
    def getString: Option[String] = None
}

final case class FloatTableCellValue(value: Float) extends TableCellValue {
    def getInt: Option[Int] = None
    def getFloat: Option[Float] = Some(value)
    def getString: Option[String] = None
}

final case class StringTableCellValue(value: String) extends TableCellValue {
    def getInt: Option[Int] = None
    def getFloat: Option[Float] = None
    def getString: Option[String] = Some(value)
}
