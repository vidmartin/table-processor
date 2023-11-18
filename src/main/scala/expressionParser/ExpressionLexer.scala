package expressionParser

import scala.collection.AbstractIterator

class ExpressionLexer(val source: String) extends Iterable[Token] {
    val KEYWORDS = Array('+', '-', '*', '/', '%', '(', ')')

    override def iterator: Iterator[Token] = new AbstractIterator[Token] {
        var index: Int = 0
        def hasNext: Boolean = index < source.length()
        def next(): Token = {
            while (source(index).isWhitespace) {
                index += 1
            }

            if (source(index).isDigit) {
                val preDot = source.substring(index).takeWhile(_.isDigit)
                index += preDot.length()

                var dot = false
                var postDot = ""
                if (index < source.length() && source(index) == '.') {
                    index += 1
                    dot = true
                    postDot = source.substring(index).takeWhile(_.isDigit)
                    index += postDot.length()
                }

                if (dot) {
                    return FloatToken((preDot + "." + postDot).toFloat)
                } else {
                    return IntToken(preDot.toInt)
                }
            } else if (source(index) == '"') {
                index += 1
                
                var escape = false
                var string = ""
                while (source(index) != '"' || escape) {
                    if (escape) {
                        escape = false
                        string += source(index)
                    } else if (source(index) == '\\') {
                        escape = true
                    } else {
                        string += source(index)
                    }
                    index += 1
                }
                
                index += 1
                return StringToken(string)
            } else if (KEYWORDS.contains(source(index))) {
                val token = KeywordToken(source(index).toString)
                index += 1
                return token
            } else if (source(index).isLetter) {
                val start = index
                while (index < source.length() && source(index).isLetterOrDigit) {
                    index += 1
                }
                return IdentifierToken(source.substring(start, index))
            }
            throw new Exception() // TODO: more specific exception
        }
    }
}
