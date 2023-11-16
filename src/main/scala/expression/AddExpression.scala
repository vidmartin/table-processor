package expression

import table.TableCellPosition

final case class AddExpression(
    override val lhs: Expression,
    override val rhs: Expression
) extends BinaryExpression {
    override def getInt(lhs: Int, rhs: Int): Option[Int] = Some(lhs + rhs)
    override def getFloat(lhs: Double, rhs: Double): Option[Double] = Some(lhs + rhs)
    override def getString(lhs: String, rhs: String): Option[String] = Some(lhs + rhs)
}