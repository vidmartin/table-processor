
package tablePrinter

import expression.ConstantExpression
import expression.FloatExpression
import expression.IntExpression
import expression.StringExpression
import expression.EmptyExpression

/** knows how to convert ConstantExpressions to Strings */
object ConstantExpressionFormatter extends ExpressionFormatter[ConstantExpression] {
    override def format(expression: ConstantExpression): String = expression match {
        case _: EmptyExpression.type => ""
        case FloatExpression(v) => v.toString()
        case IntExpression(v) => v.toString()
        case StringExpression(s) => s 
    }
}
