package expression

import table.TableCellPosition

abstract class ConstantExpression extends VariantExpression[Unit] {
    def getOne: Option[ConstantExpression] = Some(this)
    override def isAtomic: Boolean = true
    override def referencedPositions: Iterable[TableCellPosition] = Iterable.empty

    def getInt: Option[Int]
    def getFloat: Option[Double]
    def getString: Option[String]

    final override def prepare(context: ExpressionEvaluationContext): Unit = ()
    final override def getInt(unit: Unit): Option[Int] = getInt
    final override def getFloat(unit: Unit): Option[Double] = getFloat
    final override def getString(unit: Unit): Option[String] = getString
}

final case object EmptyExpression extends ConstantExpression {
    override def getInt: Option[Int] = None
    override def getFloat: Option[Double] = None
    override def getString: Option[String] = None
}
final case class IntExpression(value: Int) extends ConstantExpression {
    override def getInt: Option[Int] = Some(value)
    override def getFloat: Option[Double] = Some(value.toDouble)
    override def getString: Option[String] = None
}
final case class FloatExpression(value: Double) extends ConstantExpression {
    override def getInt: Option[Int] = None
    override def getFloat: Option[Double] = Some(value)
    override def getString: Option[String] = None
}
final case class StringExpression(value: String) extends ConstantExpression {
    override def getInt: Option[Int] = None
    override def getFloat: Option[Double] = None
    override def getString: Option[String] = Some(value)
}
