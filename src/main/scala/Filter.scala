
abstract class Filter

class ValueFilter(
    val column: String,
    val operator: String,
    val value: Float,
)

class EmptyFilter(
    val column: String,
    val shouldBeEmpty: Boolean,
)
