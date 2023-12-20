
package tablePrinter

import expression.Expression

/** knows how to convert some subtype of Expression to Strings */
abstract class ExpressionFormatter[-T <: Expression] {
    def format(expression: T): String
}
