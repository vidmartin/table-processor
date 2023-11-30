
package stringWriter

import java.io.Closeable

trait StringWriter extends Closeable {
    def write(s: String): Unit
    def writeln(s: String): Unit
}
