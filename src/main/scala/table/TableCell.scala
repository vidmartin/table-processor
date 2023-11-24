
package table

import expression.Expression
import expression.FloatExpression
import expression.IntExpression
import expression.StringExpression
import expressionParser.ExpressionLexer
import expressionParser.ExpressionParser

case class TableCell[T <: Expression](expr: T)

object TableCell {
    def parse(string: String): Option[TableCell[Expression]] = {
        if (string.startsWith("=")) {
            val lex = new ExpressionLexer(string.substring(1))
            val par = new ExpressionParser(lex)
            Some(new TableCell(par.getExpression()))
        } else if (string.forall(_.isWhitespace)) {
            None
        } else {
            (string.toIntOption, string.toDoubleOption) match {
                case (None, Some(v)) => Some(new TableCell(FloatExpression(v)))
                case (Some(v), _) => Some(new TableCell(IntExpression(v)))
                case _ => Some(new TableCell(StringExpression(string)))
            }
        }
    }
}
