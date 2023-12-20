
package evaluation

import table._
import expression.Expression
import expression.ConstantExpression

/** knows how to evaluate a table - i.e. convert all Expressions therein to ConstantExpressions */
abstract class BaseTableEvaluator {
    def evaluateTable(table: Table[Expression]): Table[ConstantExpression]
}