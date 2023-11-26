
package tablePrinter

trait StringWriter {
    def write(s: String): Unit
    def writeln(s: String): Unit
}
