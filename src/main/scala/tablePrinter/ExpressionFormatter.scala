
package tablePrinter

import expression.Expression

abstract class ExpressionFormatter[-T <: Expression] {
    def format(expression: T): String
}
