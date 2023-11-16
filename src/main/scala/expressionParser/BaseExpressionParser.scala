package expressionParser

import expression.Expression

abstract class BaseExpressionParser {
    def parse(str: String): Option[Expression]
}
