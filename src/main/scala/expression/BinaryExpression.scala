package expression

import table.TableCellPosition

case class BinaryExpressionTemp(lhs: ConstantExpression, rhs: ConstantExpression) {
    lhs match {
        case EmptyExpression => throw new EmptyOperandException(f"left side of binary expression is empty!")
        case _ => {}
    }
    rhs match {
        case EmptyExpression => throw new EmptyOperandException(f"right side of binary expression is empty!")
        case _ => {}
    }
}

abstract class BinaryExpression extends VariantExpression[BinaryExpressionTemp] {
    def lhs: Expression
    def rhs: Expression

    final override def prepare(context: ExpressionEvaluationContext): BinaryExpressionTemp = {
        BinaryExpressionTemp(lhs.evaluate(context), rhs.evaluate(context))
    }

    override def failure(context: BinaryExpressionTemp): Nothing = {
        throw new IncompatibleOperandsException(
            f"can't perform ${this.getClass().getName()} operation " +
            f"with ${context.lhs.getClass().getName()} on left-hand side and " +
            f"${context.rhs.getClass().getName()} on right-hand side"
        )
    }

    final override def getInt(temp: BinaryExpressionTemp): Option[Int] = {
        (temp.lhs.getInt, temp.rhs.getInt) match {
            case (Some(l), Some(r)) => getInt(l, r)
            case _ => None
        }
    }
    final override def getFloat(temp: BinaryExpressionTemp): Option[Double] = {
        (temp.lhs.getFloat, temp.rhs.getFloat) match {
            case (Some(l), Some(r)) => getFloat(l, r)
            case _ => None
        }
    }
    final override def getString(temp: BinaryExpressionTemp): Option[String] = {
        (temp.lhs.getString, temp.rhs.getString) match {
            case (Some(l), Some(r)) => getString(l, r)
            case _ => None
        }
    }

    def referencedPositions: Iterable[TableCellPosition] = Set.from(
        Iterable.concat(lhs.referencedPositions, rhs.referencedPositions)
    )

    def getInt(lhs: Int, rhs: Int): Option[Int]
    def getFloat(lhs: Double, rhs: Double): Option[Double]
    def getString(lhs: String, rhs: String): Option[String]
}