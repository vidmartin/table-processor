package expressionParser

import scala.collection.AbstractIterator

class ExpressionLexer(val source: String) extends Iterable[Token] {
    val KEYWORDS = Array('+', '-', '*', '/', '%', '(', ')')

    override def iterator: Iterator[Token] = new AbstractIterator[Token] {
        var index: Int = 0
        def hasNext: Boolean = index < source.length()
        def next(): Token = {
            if (source(index).isDigit) {
                val preDot = source.substring(index).takeWhile(_.isDigit)
                index += preDot.length()

                var dot = false
                var postDot = ""
                if (source(index) == '.') {
                    index += 1
                    dot = true
                    postDot = source.substring(index).takeWhile(_.isDigit)
                    index += postDot.length()
                }

                if (dot) {
                    FloatToken((preDot + "." + postDot).toFloat)
                } else {
                    IntToken(preDot.toInt)
                }
            } else if (source(index) == '"') {
                val start = index
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
                StringToken(string)
            } else if (KEYWORDS.contains(source(index))) {
                index += 1
                KeywordToken(source(index).toString)
            }
            throw new Exception() // TODO: more specific exception
        }
    }
}
