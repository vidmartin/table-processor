package expression

final case class DivideExpression(
    override val lhs: Expression,
    override val rhs: Expression
) extends BinaryExpression {
    override def getInt(lhs: Int, rhs: Int): Option[Int] = if (lhs % rhs == 0) Some(lhs / rhs) else None
    override def getFloat(lhs: Double, rhs: Double): Option[Double] = Some(lhs / rhs)
    override def getString(lhs: String, rhs: String): Option[String] = None
}