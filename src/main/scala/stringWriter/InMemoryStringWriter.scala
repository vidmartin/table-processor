
package stringWriter

class InMemoryStringWriter extends StringWriter {
    private val builder = new StringBuilder()
    override def write(s: String): Unit = builder.append(s)
    override def writeln(s: String): Unit = {
        builder.append(s)
        builder.append("\n")
    }
    override def close(): Unit = {}

    def get: String = builder.toString()
}
