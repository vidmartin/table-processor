
import cliOpts.OptRegistry
import stringWriter.StringWriter

/** knows how to print information about how to use this program to the user */
abstract class HelpPrinter {
    def printHelp(registry: OptRegistry, programDescription: String, output: StringWriter): Unit
}
