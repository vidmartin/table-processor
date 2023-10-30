
class Opts(
    val inputFile: String, // only mandatory param
    val separator: String,
    val format: SupportedFormat,
    val outputSeparator: String,
    val headers: Boolean,
    val outputFile: Option[String],
    val stdout: Boolean,
    val help: Boolean,
    val filters: List[Filter],
    val range: Option[Range],
)
