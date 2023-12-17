
import cliOpts._
import cliOpts.cliOpt._
import cliOpts.optRegister._
import filters._
import expression._
import scala.collection.mutable.ArrayBuffer
import stringWriter.StdoutStringWriter

object TableProcessorOptsLoader {
    val programDescription = 
        "vidmartin's Table Processor\n===========================\n" +
        "Loads a file describing a table, evaluates expressions therein and outputs the result.\n" +
        "Accepts only named options as described below. Required options are marked with '!'."

    def runWithOpts(args: Iterable[String], helpPrinter: HelpPrinter)(callback: Opts => Unit): Unit = {
        val optsReg = new OptRegistry
        val filters = new ArrayBuffer[RowFilter[ConstantExpression]]

        val inputFile = optsReg.addOption(new RequiredCommandLineOption(
            new StringOptRegister, "input-file", Some('i'),
            "the path to the file to be processed",
            Some("[FILENAME]")
        ))
        val separator = optsReg.addOption(new CommandLineOption(
            new StringOptRegister().withDefault(","), "separator", None,
            "the separator to be used for parsing the input CSV file"
        ))
        val format = optsReg.addOption(new CommandLineOption(
            new EnumOptRegister(SupportedFormat).withDefault(SupportedFormat.CSV),
            "format", Some('F'),
            "the format of the output file"
        ))
        val outputSeparator = optsReg.addOption(new CommandLineOption(
            new StringOptRegister().withDefault(","), "output-separator", None,
            "the separator to be used for the output file, in case the selected format is CSV",
        ))
        val headers = optsReg.addOption(new CommandLineOption(
            new FlagOptRegister, "headers", Some('H'),
            "whether to include header row in the output"
        ))
        val outputFile = optsReg.addOption(new CommandLineOption(
            new StringOptRegister, "output-file", Some('o'),
            "the file to write the output into; if unspecified, write to STDOUT",
            Some("[FILENAME]")
        ))
        val stdout = optsReg.addOption(new CommandLineOption(
            new FlagOptRegister, "stdout", None,
            "whether to write the output to STDOUT; if output file is not specified, this is considered to be true implicitly"
        ))
        val filter = optsReg.addOption(new CommandLineOption(
            new ValueFilterOptRegister(filters.addOne(_)), "filter", None,
            "when specified, all rows that don't satisfy the given condition will be excluded from the result (may be specified multiple times)",
        ))
        val filterIsEmpty = optsReg.addOption(new CommandLineOption(
            new ColumnPredicateFilterOptRegister(col => filters.addOne(new EmptinessFilter(col, true))), "filter-is-empty", None,
            "when specified, only rows where the given column is empty will be outputted (may be specified multiple times)",
        ))
        val filterIsNotEmpty = optsReg.addOption(new CommandLineOption(
            new ColumnPredicateFilterOptRegister(col => filters.addOne(new EmptinessFilter(col, false))), "filter-is-not-empty", None,
            "when specified, rows where the given column is empty will be excluded from the result (may be specified multiple times)",
        ))
        val range = optsReg.addOption(new CommandLineOption(
            new RangeOptRegister, "range", None,
            "only output cells from the given range",
        ))
        val help = optsReg.addOption(new CommandLineOption(
            new FlagOptRegister, "help", Some('h'),
            "when specified, don't do anything, just print help and exit",
        ))

        val shouldPrintHelp = try {
            !ArgLoader.load(optsReg, args, Some(() => help.optRegister.get)) || help.optRegister.get
        } catch {
            case e: ArgException => {
                println(f"\u001b[31mError while parsing args:\n   ${e.getMessage()}\u001b[0m\n")
                true
            }
        }

        if (shouldPrintHelp) {
            helpPrinter.printHelp(optsReg, programDescription, new StdoutStringWriter())
            return
        }

        val opts = Opts(
            inputFile = inputFile.optRegister.get,
            separator = separator.optRegister.get,
            format = format.optRegister.get,
            outputSeparator = outputSeparator.optRegister.get,
            headers = headers.optRegister.get,
            outputFile = outputFile.optRegister.getOptional,
            stdout = stdout.optRegister.get,
            filters = List.from(filters),
            range = range.optRegister.getOptional
        )

        callback(opts)
    }
}
