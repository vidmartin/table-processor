package expressionParser

import org.scalatest.FunSuite
import expression.MultiplyExpression
import expression.AddExpression
import expression.IntExpression
import expression.ReferenceExpression
import table.TableCellPosition
import expression.StringExpression

class ExpressionParserTest extends FunSuite {
    test("test1") {
        // (7 + 3 * D4) * "ahoj"
        val parser = new ExpressionParser(Array(
            KeywordToken("("),
            IntToken(7),
            KeywordToken("+"),
            IntToken(3),
            KeywordToken("*"),
            IdentifierToken("D4"),
            KeywordToken(")"),
            KeywordToken("*"),
            StringToken("ahoj"),
        ))
        assert(parser.getExpression() == MultiplyExpression(
            AddExpression(
                IntExpression(7),
                MultiplyExpression(
                    IntExpression(3),
                    ReferenceExpression(TableCellPosition.parse("D4").get)
                )
            ),
            StringExpression("ahoj")
        ))
    }

    test("test2") {
        // 6 * 2 + + 8
        val parser = new ExpressionParser(Array(
            IntToken(6),
            KeywordToken("*"),
            IntToken(2),
            KeywordToken("+"),
            KeywordToken("+"),
            IntToken(8),
        ))
        assertThrows[ExpressionParsingException] {
            parser.getExpression()
        }
    }
}