
package cliOpts.optRegister

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
