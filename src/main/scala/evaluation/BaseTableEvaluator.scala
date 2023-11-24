
package evaluation

import table._
import expression.Expression
import expression.ConstantExpression

abstract class BaseTableEvaluator {
    def evaluateTable(table: Table[Expression]): Table[ConstantExpression]
}