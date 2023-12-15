
package filters

import enum._

sealed case class Comparator(override val name: String) extends EnumCase
object Comparator extends EnumCompanion[Comparator] {
    object LT extends Comparator("<")
    object GT extends Comparator(">")
    object LE extends Comparator("<=")
    object GE extends Comparator(">=")
    object EQ extends Comparator("==")
    object NE extends Comparator("!=")

    override val all = Array(LT, GT, LE, GE, EQ, NE)
}
