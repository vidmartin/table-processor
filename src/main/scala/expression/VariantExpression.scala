package expression

abstract class VariantExpression[T] extends Expression {
    override def evaluate(context: ExpressionEvaluationContext): ConstantExpression = {
        val temp = prepare(context)
        None.orElse(
            getInt(temp).map(IntExpression(_))
        ).orElse(
            getFloat(temp).map(FloatExpression(_))
        ).orElse(
            getString(temp).map(StringExpression(_))
        ).getOrElse(failure(temp))
    }

    def prepare(context: ExpressionEvaluationContext): T
    def getInt(info: T): Option[Int]
    def getFloat(info: T): Option[Double]
    def getString(info: T): Option[String]
    protected def failure(info: T): Nothing
}