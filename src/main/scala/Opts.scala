
import filters.RowFilter
import expression.ConstantExpression
import table.TableCellRange

abstract class BaseOpts {
    def showHelp: Boolean
}

final object ShowHelpOpts extends BaseOpts {
    def showHelp: Boolean = true
}

final case class Opts(
    inputFile: String, // only mandatory param
    separator: String,
    format: SupportedFormat,
    outputSeparator: String,
    headers: Boolean,
    outputFile: Option[String],
    stdout: Boolean,
    filters: List[RowFilter[ConstantExpression]],
    range: Option[TableCellRange],
) extends BaseOpts {
    def showHelp: Boolean = false
}
