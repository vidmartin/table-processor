
package stringWriter

/** writes strings to STDOUT */
class StdoutStringWriter extends StringWriter {
    override def write(s: String): Unit = print(s)
    override def writeln(s: String): Unit = println(s)
    override def close(): Unit = {}
}
