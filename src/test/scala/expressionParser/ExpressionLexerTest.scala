package expressionParser

import org.scalatest.FunSuite

class ExpressionLexerTest extends FunSuite {
    test("test1") {
        val lexer = new ExpressionLexer("5 + (3 * 7)")
        val arr = List.from(lexer)
        assert(arr == List(
            IntToken(5),
            KeywordToken("+"),
            KeywordToken("("),
            IntToken(3),
            KeywordToken("*"),
            IntToken(7),
            KeywordToken(")"),
        ))
    }

    test("test2") {
        val lexer = new ExpressionLexer("\"hello\" + \" \" + \"world!\"")
        val arr = List.from(lexer)
        assert(arr == List(
            StringToken("hello"),
            KeywordToken("+"),
            StringToken(" "),
            KeywordToken("+"),
            StringToken("world!"),
        ))
    }

    test("test3") {
        val lexer = new ExpressionLexer("\"\\\\my name is \\\"Martin\\\"\\\\\"")
        val arr = List.from(lexer)
        assert(arr == List(
            StringToken("\\my name is \"Martin\"\\")
        ))
    }

    test("test4") {
        val lexer = new ExpressionLexer("(A3 + D5) / 2")
        val arr = List.from(lexer)
        assert(arr == List(
            KeywordToken("("),
            IdentifierToken("A3"),
            KeywordToken("+"),
            IdentifierToken("D5"),
            KeywordToken(")"),
            KeywordToken("/"),
            IntToken(2),
        ))
    }
}