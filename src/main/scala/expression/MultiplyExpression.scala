package expression

final case class MultiplyExpression(
    override val lhs: Expression,
    override val rhs: Expression
) extends BinaryExpression {
    override def getInt(lhs: Int, rhs: Int): Option[Int] = Some(lhs * rhs)
    override def getFloat(lhs: Double, rhs: Double): Option[Double] = Some(lhs * rhs)
    override def getString(lhs: String, rhs: String): Option[String] = None
}
