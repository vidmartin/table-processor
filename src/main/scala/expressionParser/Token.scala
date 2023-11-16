package expressionParser

abstract class Token
case class IntToken(value: Int) extends Token
case class FloatToken(value: Double) extends Token
case class StringToken(value: String) extends Token
case class KeywordToken(keyword: String) extends Token
