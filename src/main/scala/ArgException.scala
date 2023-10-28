
class ArgException(message: String) extends Exception(message)
class MultipleOccurencesArgException(message: String) extends ArgException(message)
class AlreadyExistsArgException(message: String) extends ArgException(message)
class UnexpectedPositionArgException(message: String) extends ArgException(message)
class UnknownArgException(message: String) extends ArgException(message)
class MissingValueArgException(message: String) extends ArgException(message)
