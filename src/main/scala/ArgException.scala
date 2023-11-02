
abstract class ArgException(message: String) extends Exception(message)
final case class MultipleOccurencesArgException(message: String) extends ArgException(message)
final case class AlreadyExistsArgException(message: String) extends ArgException(message)
final case class UnexpectedPositionArgException(message: String) extends ArgException(message)
final case class UnknownArgException(message: String) extends ArgException(message)
final case class IncompleteArgException(message: String) extends ArgException(message)
final case class MissingArgException(message: String) extends ArgException(message)
final case class FormatArgException(message: String) extends ArgException(message)
