
abstract class BaseOpt {
    /** parses the value from the iterator of arguments (does nothing if isDefined is true) */
    def load(it: Iterator[String]): Unit = {
        if (!this.isDefined) {
            this.forceLoad(it)
        }
    }
    /** parses the value from the iterator of arguments */
    protected def forceLoad(it: Iterator[String]): Unit
    /** whether a value was specified by the user or a default value is available */
    def hasValue: Boolean
    /** whether an error is to be thrown when hasValue returns false */
    def isRequired: Boolean
    /** whether a value for this argument is fully defined */
    def isDefined: Boolean
}

abstract class Opt[T] extends BaseOpt {
    /** returns the parsed value or a default value; if neither is available, throw exception */
    def get: T
}

class FlagOpt extends Opt[Boolean] {
    private var flagSet: Boolean = false

    override def get: Boolean = flagSet
    override def forceLoad(it: Iterator[String]): Unit = {
        flagSet = true
    }

    override def hasValue: Boolean = true
    override def isRequired: Boolean = false
    override def isDefined: Boolean = flagSet
}

class MapOpt[T](
    mapper: String => T,
    required: Boolean = false
) extends Opt[T] {
    protected var default: Option[T] = None
    protected var value: Option[T] = None

    override def get: T = value.orElse(default).get
    override def forceLoad(it: Iterator[String]): Unit = {
        value = Some(mapper(it.next()))
    }
    override def hasValue: Boolean = value.isDefined || default.isDefined
    override def isRequired: Boolean = required
    override def isDefined: Boolean = value.isDefined

    def withDefault(default: T): MapOpt[T] = {
        this.default = Some(default)
        this
    }
}

class StringOpt(required: Boolean = false)
extends MapOpt[String](s => s, required) {
    override def withDefault(default: String): StringOpt = {
        this.default = Some(default)
        this
    }
}

// TODO: range opt
// TODO: multi opt
