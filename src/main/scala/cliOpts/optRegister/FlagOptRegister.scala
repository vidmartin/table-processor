
package cliOpts.optRegister

class FlagOptRegister extends OptRegister[Boolean] {
    private var flagSet: Boolean = false

    override def getOptional: Option[Boolean] = Some(flagSet)
    override def forceLoad(it: Iterator[String]): Unit = {
        flagSet = true
    }

    override def hasValue: Boolean = true
    override def isDefined: Boolean = flagSet
    override def hasArgs: Boolean = false
}
