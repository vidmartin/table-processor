
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

object Main extends App {
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

    TableProcessorOptsLoader.runWithOpts(this.args) {
        opts => run(opts)
    }
}
