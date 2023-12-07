
import enum._

sealed case class SupportedFormat(override val name: String) extends EnumCase
object SupportedFormat extends EnumCompanion[SupportedFormat] {
    object CSV extends SupportedFormat("csv")
    object MD extends SupportedFormat("md")

    override val all = Array(CSV, MD)
}
