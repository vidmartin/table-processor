
package stringWriter

class MultiStringWriter(children: Iterable[StringWriter]) extends StringWriter {
    override def write(s: String): Unit = {
        children.foreach(sw => sw.write(s))
    }
    override def writeln(s: String): Unit = {
        children.foreach(sw => sw.writeln(s))
    }
    override def close(): Unit = {
        children.foreach(sw => sw.close())
    }
}
