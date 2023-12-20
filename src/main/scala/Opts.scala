
import filters.RowFilter
import expression.ConstantExpression
import table.TableCellRange

/** represents the parsed CLI args for this program */
final case class Opts(
    inputFile: String,
    separator: String,
    format: SupportedFormat,
    outputSeparator: String,
    headers: Boolean,
    outputFile: Option[String],
    stdout: Boolean,
    filters: List[RowFilter[ConstantExpression]],
    range: Option[TableCellRange],
)
