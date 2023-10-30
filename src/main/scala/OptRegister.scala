
abstract class BaseOptRegister {
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
    /** whether a value for this argument is fully defined */
    def isDefined: Boolean
}

abstract class OptRegister[T] extends BaseOptRegister {
    /** returns the parsed value or a default value; if neither is available, throw exception */
    def get: T = getOptional.get
    def getOptional: Option[T]
}

class FlagOptRegister extends OptRegister[Boolean] {
    private var flagSet: Boolean = false

    override def getOptional: Option[Boolean] = Some(flagSet)
    override def forceLoad(it: Iterator[String]): Unit = {
        flagSet = true
    }

    override def hasValue: Boolean = true
    override def isDefined: Boolean = flagSet
}

abstract class ParseOptRegister[T] extends OptRegister[T] {
    protected var default: Option[T] = None
    protected var value: Option[T] = None

    protected def parse(s: String): T
    override def getOptional: Option[T] = value.orElse(default)
    override def forceLoad(it: Iterator[String]): Unit = {
        value = Some(parse(it.next()))
    }
    override def hasValue: Boolean = value.isDefined || default.isDefined
    override def isDefined: Boolean = value.isDefined

    def withDefault(default: T): ParseOptRegister[T] = {
        this.default = Some(default)
        this
    }
}

class StringOptRegister extends ParseOptRegister[String] {
    override def withDefault(default: String): StringOptRegister = {
        this.default = Some(default)
        this
    }

    override def parse(s: String): String = s
}

class EnumOptRegister[T <: EnumCase](companion: EnumCompanion[T]) extends ParseOptRegister[T] {
    override def withDefault(default: T): EnumOptRegister[T] = {
        this.default = Some(default)
        this
    }
    override def parse(s: String): T = companion.parse(s).getOrElse(
        throw new FormatArgException(f"invalid value ${s} - expected one of {${companion.all.map(c => c.name).mkString(", ")}}")
    )
}

// TODO: range opt
// TODO: multi opt
