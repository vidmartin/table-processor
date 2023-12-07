import cliOpts.cliOpt.{CommandLineOption, RequiredCommandLineOption}
import cliOpts.optRegister.{StringOptRegister, FlagOptRegister, EnumOptRegister}

import tableReader.TableReader
import tableReader.CsvReader
import config.CsvConfig
import scala.io.Source
import table.Table
import evaluation.TopSortTableEvaluator
import scala.util.Using
import java.io.PrintStream
import tablePrinter.CsvTablePrinter
import tablePrinter.ConstantExpressionFormatter
import java.io.File
import scala.Console
import java.util.stream
import stringWriter.StringWriter
import tablePrinter.TableViewWithHeaders
import tablePrinter.TablePrinter
import SupportedFormat.CSV
import SupportedFormat.MD
import tablePrinter.MarkdownTablePrinter
import expression.ConstantExpression
import expression.Expression
import table.TableView
import expression.StringExpression
import stringWriter._
import cliOpts._

object Main extends App {
    def loadOpts(): BaseOpts = {
        val opts = new OptRegistry

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
            filters = List(),
            range = None
        )
    }

    def run(opts: Opts): Unit = {
        println(f"loading file ${opts.inputFile}")
        val inputTable = getInputTable(opts)
        val resultTable = getResultTable(opts, inputTable)
        val outputView = getOutputView(opts, resultTable)
        val tablePrinter = getTablePrinter(opts)
        Using(getOutputStringWriter(opts)) {
            outputStringWriter => tablePrinter.printTable(outputView, outputStringWriter)
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

    def getOutputView[T >: StringExpression <: Expression](opts: Opts, table: Table[T]): TableView[T] = {
        if (opts.headers) {
            new TableViewWithHeaders(table)
        } else {
            table
        }
    }

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
