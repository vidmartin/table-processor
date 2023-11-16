package expressionParser

class ExpressionLexer extends BaseExpressionLexer[Token] {
    override def tokenize(str: String): Iterable[Token] = {
    }
    def getToken(str: String): Option[Token]
}
