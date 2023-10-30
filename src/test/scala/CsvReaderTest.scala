
import org.scalatest.FunSuite
import tableReader.CsvReader
import scala.io.Source
import tableReader.CsvConfig

class CsvReaderTest extends FunSuite {
    test("test1") {
        val table = "a,b,c,d\ne,f,g,h"
        val reader = new CsvReader(Source.fromString(table), new CsvConfig(","))
        val list = reader.toList

        assert(list == List(
            List("a", "b", "c", "d"),
            List("e", "f", "g", "h")
        ))
    }

    test("test2") {
        val table = "ahoj|hello|hallo\npes|dog|hund\nstrom|tree|baum\nsejr,|cheese,\n"
        val reader = new CsvReader(Source.fromString(table), new CsvConfig("|"))
        val list = reader.toList

        assert(list == List(
            List("ahoj", "hello", "hallo"),
            List("pes", "dog", "hund"),
            List("strom","tree", "baum"),
            List("sejr,", "cheese,")
        ))
    }

    test("test3") {
        val table = "CZ$$EN$$US\n\nCZK (Kč)$$GBP (£)$$USD ($)\nkoruna$$libra$$dolar\n"
        val reader = new CsvReader(Source.fromString(table), new CsvConfig("$$"))
        val list = reader.toList

        assert(list == List(
            List("CZ", "EN", "US"),
            List("CZK (Kč)", "GBP (£)", "USD ($)"),
            List("koruna", "libra", "dolar")
        ))
    }

    test("test4") {
        val table = ""
        val reader = new CsvReader(Source.fromString(table), new CsvConfig("$$"))
        val list = reader.toList

        assert(list == List())
    }
}