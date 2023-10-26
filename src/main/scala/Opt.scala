
abstract class BaseOpt {
    /** parses the value from the iterator of arguments */
    def load(it: Iterator[String]): Unit
    /** whether a value was specified by the user or a default value is available */
    def hasValue: Boolean
    /** whether an error is to be thrown when hasValue returns false */
    def isRequired: Boolean
    /** whether a value was specified by the user */
    def isSpecified: Boolean
}

abstract class Opt[T] extends BaseOpt {
    /** returns the parsed value or a default value; if neither is available, throw exception */
    def get: T
}

class FlagOpt extends Opt[Boolean] {
    private var flagSet: Boolean = false

    override def get: Boolean = flagSet
    override def load(it: Iterator[String]): Unit = {
        if (flagSet) {
            throw new Exception() // TODO: more specific exception
        }
        flagSet = true
    }

    override def hasValue: Boolean = true
    override def isRequired: Boolean = false
    override def isSpecified: Boolean = flagSet
}

class MapOpt[T](
    required: Boolean,
    mapper: String => T,
    default: Option[T] = None,
) extends Opt[T] {
    private var value: Option[T] = None

    override def get: T = value.orElse(default).get
    override def load(it: Iterator[String]): Unit = {
        if (this.isSpecified) {
            throw new Exception() // TODO: more specific exception
        }

        if (it.hasNext) {
            value = Some(mapper(it.next()))
        } else {
            throw new Exception() // TODO: more specific exception
        }
    }
    override def hasValue: Boolean = value.isDefined || default.isDefined
    override def isRequired: Boolean = required
    override def isSpecified: Boolean = value.isDefined

    def this(mapper: String => T, default: T) = this(false, mapper, Some(default))
}

class StringOpt(required: Boolean, default: Option[String] = None)
extends MapOpt[String](required, s => s, default) {
    def this(default: String) = this(false, Some(default))
}

// TODO: range opt
// TODO: multi opt
