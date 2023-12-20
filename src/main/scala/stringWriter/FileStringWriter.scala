
package stringWriter

import java.io.FileWriter

/** writes strings to a file */
class FileStringWriter(fileName: String) extends StringWriter {
    val fileWriter = new FileWriter(fileName)
    override def write(s: String): Unit = {
        fileWriter.write(s)
    }
    override def writeln(s: String): Unit = {
        fileWriter.write(s)
        fileWriter.write("\n")
    }
    override def close(): Unit = {
        fileWriter.close()
    }
}
