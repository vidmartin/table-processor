
import cliOpts._
import cliOpts.cliOpt._
import cliOpts.optRegister._
import config._
import expression._
import filters._
import table._
import tableReader._
import tablePrinter._
import stringWriter._
import evaluation.TopSortTableEvaluator
import SupportedFormat._
import scala.io.Source
import scala.util.Using
import java.io.PrintStream
import java.io.File
import scala.Console
import java.util.stream
import scala.collection.mutable.ArrayBuffer

object Main extends App {
    def loadOpts(): BaseOpts = {
        val opts = new OptRegistry
        val filters = new ArrayBuffer[RowFilter[ConstantExpression]]

        val inputFile = opts.addOption(new RequiredCommandLineOption(
            new StringOptRegister, "input-file", Some('i'),
            "the path to the file to be processed"
        ))
        val separator = opts.addOption(new CommandLineOption(
            new StringOptRegister().withDefault(","), "separator", None,
            "the separator to be used for parsing the input CSV file"
        ))
        val format = opts.addOption(new CommandLineOption(
            new EnumOptRegister(SupportedFormat).withDefault(SupportedFormat.CSV),
            "format", None,
            "the format of the output file"
        ))
        val outputSeparator = opts.addOption(new CommandLineOption(
            new StringOptRegister().withDefault(","), "output-separator", None,
            "the separator to be used for the output file, in case the selected format is CSV"
        ))
        val headers = opts.addOption(new CommandLineOption(
            new FlagOptRegister, "headers", None,
            "whether to include header row in the output"
        ))
        val outputFile = opts.addOption(new CommandLineOption(
            new StringOptRegister, "output-file", Some('o'),
            "the file to write the output into; if unspecified, write to STDOUT"
        ))
        val stdout = opts.addOption(new CommandLineOption(
            new FlagOptRegister, "stdout", None,
            "whether to write the output to STDOUT; if output file is not specified, this is considered to be true implicitly"
        ))
        val filter = opts.addOption(new CommandLineOption(
            new ValueFilterOptRegister(filters.addOne(_)), "filter", None,
            "usage: --filter [COLUMN] [OPERATOR] [VALUE]. This option may be specified multiple times. For each occurence of this option, all rows that don't satisfy the given condition will be excluded from the result."
        ))
        val filterIsEmpty = opts.addOption(new CommandLineOption(
            new ColumnPredicateFilterOptRegister(col => filters.addOne(new EmptinessFilter(col, true))), "filter-is-empty", None,
            "usage: --filter-is-empty [COLUMN]. When specified, only rows where the given column is empty will be outputted."
        ))
        val filterIsNotEmpty = opts.addOption(new CommandLineOption(
            new ColumnPredicateFilterOptRegister(col => filters.addOne(new EmptinessFilter(col, false))), "filter-is-not-empty", None,
            "usage: --filter-is-not-empty [COLUMN]. When specified, rows where the given column is empty will be excluded from the result."
        ))
        val range = opts.addOption(new CommandLineOption(
            new RangeOptRegister, "range", None,
            "usage: --range [UPPER LEFT] [LOWER RIGHT]. Only output cells from the given range."
        ))
        val help = opts.addOption(new CommandLineOption(
            new FlagOptRegister, "help", Some('h'),
            "when specified, don't do anything, just print help and exit"
        ))

        
        if (!ArgLoader.load(opts, this.args, Some(() => help.optRegister.get))) {
            return ShowHelpOpts
        }

        return Opts(
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
    }

    def run(opts: Opts): Unit = {
        println(f"loading file ${opts.inputFile}")
        val inputTable = getInputTable(opts)
        val resultTable = getResultTable(opts, inputTable)
        val printOptions = getPrintOptions(opts, resultTable)
        val tablePrinter = getTablePrinter(opts)
        Using(getOutputStringWriter(opts)) {
            outputStringWriter => tablePrinter.printTable(printOptions, outputStringWriter)
        }
    }

    def getInputTable(opts: Opts): Table[Expression] = {
        Using(Source.fromFile(opts.inputFile)) {
            file => {
                val reader = new CsvReader(file, CsvConfig(opts.separator))
                Table.parse(reader)
            }
        }.get
    }

    def getResultTable(opts: Opts, table: Table[Expression]): Table[ConstantExpression] = {
        TopSortTableEvaluator.evaluateTable(table)
    }

    def getPrintOptions(
        opts: Opts, table: Table[ConstantExpression]
    ): TablePrintOptions[ConstantExpression] = TablePrintOptions(
        opts.range match {
            case Some(range) => new RangeTableView(table, range)
            case None => table
        },
        new AndFilter(opts.filters),
        opts.headers,
        opts.headers
    )

    def getTablePrinter(opts: Opts): TablePrinter[ConstantExpression] = {
        opts.format match {
            case CSV => new CsvTablePrinter(CsvConfig(opts.separator), ConstantExpressionFormatter)
            case MD => new MarkdownTablePrinter(ConstantExpressionFormatter)
            case _ => throw new NotImplementedError()
        }
    }

    def getOutputStringWriter(opts: Opts): StringWriter = {
        (opts.outputFile, opts.stdout) match {
            case (Some(filename), true) => new MultiStringWriter(List(
                new StdoutStringWriter(),
                new FileStringWriter(filename)
            ))
            case (None, _) => new StdoutStringWriter()
            case (Some(filename), _) => new FileStringWriter(filename)
        }
    }

    loadOpts() match {
        case ShowHelpOpts => {
            println("showing help")
        }
        case o: Opts => run(o)
    }
}
