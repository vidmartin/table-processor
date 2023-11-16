package expression

abstract class VariantExpression[T] extends Expression {
    final override def evaluate(context: ExpressionEvaluationContext): ConstantExpression = {
        val temp = prepare(context)
        None.orElse(
            getInt(temp).map(IntExpression(_))
        ).orElse(
            getFloat(temp).map(FloatExpression(_))
        ).orElse(
            getString(temp).map(StringExpression(_))
        ).get
    }

    def prepare(context: ExpressionEvaluationContext): T
    def getInt(info: T): Option[Int]
    def getFloat(info: T): Option[Double]
    def getString(info: T): Option[String]
}