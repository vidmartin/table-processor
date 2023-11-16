package expressionParser

abstract class BaseExpressionLexer[T] {
    def tokenize(str: String): Iterable[T]
}