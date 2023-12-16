
import cliOpts.OptRegistry
import stringWriter.StringWriter
import cliOpts.helpPrinter.UsageVisitor

class BasicHelpPrinter extends HelpPrinter {
    def printHelp(registry: OptRegistry, programDescription: String, output: StringWriter): Unit = {
        output.writeln(programDescription)
        output.write("\n")

        val sorted = registry.getAll.sortBy {
            case (long, short, cliOpt) => long
        }

        for ((long, short, cliOpt) <- sorted) {
            output.write(f"--${long}")
            short match {
                case Some(c) => output.write(f"/-${c}")
                case None => {}
            }
            if (cliOpt.isRequired) {
                output.write("!")
            }

            cliOpt.usage.orElse(
                cliOpt.optRegister.accept(UsageVisitor)
            ) match {
                case Some(u) => output.write(f" ${u}")
                case None => {}
            }

            output.writeln(f"\n    ${cliOpt.description}")
        }
    }
}
