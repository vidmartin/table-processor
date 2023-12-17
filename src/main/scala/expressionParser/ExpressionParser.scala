package expressionParser

import expression.Expression
import expression.ConstantExpression
import expression.IntExpression
import expression.FloatExpression
import expression.StringExpression
import expression.AddExpression
import expression.SubtractExpression
import expression.ReferenceExpression
import table.TableCellPosition
import expression.MultiplyExpression
import expression.DivideExpression
import expression.ModuloExpression

class ExpressionParser(lex: Iterable[Token]) {
    def getExpression(): Expression = {
        val it = lex.iterator.buffered
        return parseExpressionL1(it)
    }

    private def expectToken(it: Iterator[Token], tok: Token): Unit = {
        val got = it.next()
        if (got != tok) {
            throw new ExpressionParsingException(f"expected token ${tok}, got ${got} instead")
        }
    }

    private def parseExpressionL1(it: BufferedIterator[Token]): Expression = {
        val lhs = parseExpressionL2(it)
        it.headOption match {
            case Some(KeywordToken("+")) => {
                it.next()
                AddExpression(lhs, parseExpressionL1(it))
            }
            case Some(KeywordToken("-")) => {
                it.next()
                SubtractExpression(lhs, parseExpressionL1(it))
            }
            case None | Some(KeywordToken(")")) => lhs
            case what => throw new ExpressionParsingException(f"unexpected ${what} (parseExpressionL1)")
        }
    }

    private def parseExpressionL2(it: BufferedIterator[Token]): Expression = {
        val lhs = parseTerm(it)
        it.headOption match {
            case Some(KeywordToken("*")) => {
                it.next()
                MultiplyExpression(lhs, parseExpressionL2(it))
            }
            case Some(KeywordToken("/")) => {
                it.next()
                DivideExpression(lhs, parseExpressionL2(it))
            }
            case Some(KeywordToken("%")) => {
                it.next()
                ModuloExpression(lhs, parseExpressionL2(it))
            }
            case None | Some(KeywordToken("+" | "-" | ")")) => lhs
            case what => throw new ExpressionParsingException(f"unexpected ${what} (parseExpressionL2)")
        }
    }

    private def parseTerm(it: BufferedIterator[Token]): Expression = {
        it.next() match {
            case IntToken(value) => IntExpression(value)
            case FloatToken(value) => FloatExpression(value)
            case StringToken(value) => StringExpression(value)
            case IdentifierToken(identifier) => ReferenceExpression(TableCellPosition.parse(identifier).get)
            case KeywordToken("(") => {
                val expr = parseExpressionL1(it)
                expectToken(it, KeywordToken(")"))
                expr
            }
            case what => throw new ExpressionParsingException(f"unexpected ${what} (parseTerm)")
        }
    }
}
