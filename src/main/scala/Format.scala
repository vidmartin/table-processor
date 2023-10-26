
sealed abstract class SupportedFormat
object SupportedFormat {
    object CSV extends SupportedFormat
    object MD extends SupportedFormat

    def parse(s: String) {
        s.toLowerCase() match {
            case "csv" => CSV
            case "md" => MD
        }
    }
}
