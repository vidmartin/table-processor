
import cliOpts.OptRegistry
import stringWriter.StringWriter

abstract class HelpPrinter {
    def printHelp(registry: OptRegistry, programDescription: String, output: StringWriter): Unit
}
