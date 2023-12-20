
package stringWriter

import java.io.Closeable

/** a custom interface for writing strings somewhere */
trait StringWriter extends Closeable {
    def write(s: String): Unit
    def writeln(s: String): Unit
}
