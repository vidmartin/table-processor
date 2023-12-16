
import cliOpts._
import cliOpts.cliOpt._
import cliOpts.optRegister._
import filters._
import expression._
import scala.collection.mutable.ArrayBuffer

object TableProcessorOptsLoader {
    def runWithOpts(args: Iterable[String])(callback: Opts => Unit): Unit = {
        val optsReg = new OptRegistry
        val filters = new ArrayBuffer[RowFilter[ConstantExpression]]

        val inputFile = optsReg.addOption(new RequiredCommandLineOption(
            new StringOptRegister, "input-file", Some('i'),
            "the path to the file to be processed"
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
            "the separator to be used for the output file, in case the selected format is CSV"
        ))
        val headers = optsReg.addOption(new CommandLineOption(
            new FlagOptRegister, "headers", Some('H'),
            "whether to include header row in the output"
        ))
        val outputFile = optsReg.addOption(new CommandLineOption(
            new StringOptRegister, "output-file", Some('o'),
            "the file to write the output into; if unspecified, write to STDOUT"
        ))
        val stdout = optsReg.addOption(new CommandLineOption(
            new FlagOptRegister, "stdout", None,
            "whether to write the output to STDOUT; if output file is not specified, this is considered to be true implicitly"
        ))
        val filter = optsReg.addOption(new CommandLineOption(
            new ValueFilterOptRegister(filters.addOne(_)), "filter", None,
            "usage: --filter [COLUMN] [OPERATOR] [VALUE]. This option may be specified multiple times. For each occurence of this option, all rows that don't satisfy the given condition will be excluded from the result."
        ))
        val filterIsEmpty = optsReg.addOption(new CommandLineOption(
            new ColumnPredicateFilterOptRegister(col => filters.addOne(new EmptinessFilter(col, true))), "filter-is-empty", None,
            "usage: --filter-is-empty [COLUMN]. When specified, only rows where the given column is empty will be outputted."
        ))
        val filterIsNotEmpty = optsReg.addOption(new CommandLineOption(
            new ColumnPredicateFilterOptRegister(col => filters.addOne(new EmptinessFilter(col, false))), "filter-is-not-empty", None,
            "usage: --filter-is-not-empty [COLUMN]. When specified, rows where the given column is empty will be excluded from the result."
        ))
        val range = optsReg.addOption(new CommandLineOption(
            new RangeOptRegister, "range", None,
            "usage: --range [UPPER LEFT] [LOWER RIGHT]. Only output cells from the given range."
        ))
        val help = optsReg.addOption(new CommandLineOption(
            new FlagOptRegister, "help", Some('h'),
            "when specified, don't do anything, just print help and exit"
        ))

        if (!ArgLoader.load(optsReg, args, Some(() => help.optRegister.get))) {
            // TODO: show help
            throw new NotImplementedError()
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
