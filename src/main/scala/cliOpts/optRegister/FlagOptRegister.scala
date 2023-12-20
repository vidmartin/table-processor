
package cliOpts.optRegister

/** stores a boolean, by default false; any call to load will set it to true without advancing the iterator at all */
class FlagOptRegister extends OptRegister[Boolean] {
    private var flagSet: Boolean = false

    override def getOptional: Option[Boolean] = Some(flagSet)
    override def forceLoad(it: Iterator[String]): Unit = {
        flagSet = true
    }

    override def hasValue: Boolean = true
    override def isDefined: Boolean = flagSet
    override def hasArgs: Boolean = false
    override def accept[T](visitor: OptRegisterVisitor[T]): T = {
        visitor.visitFlagOptRegister(this)
    }
}
