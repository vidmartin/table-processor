
class Opts(
    val separator: String,
    val inputFile: String, // only mandatory param
    val format: String,
    val outputSeparator: String,
    val headers: Boolean,
    val filters: List[Filter],
    val range: Option[Range],
    val outputFile: String,
    val stdout: Boolean,
    val help: Boolean,
)
