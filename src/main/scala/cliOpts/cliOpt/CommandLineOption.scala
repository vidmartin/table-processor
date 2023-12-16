
package cliOpts.cliOpt

import cliOpts.optRegister._

class CommandLineOption[T](
    override val optRegister: OptRegister[T],
    override val longName: String,
    override val shortName: Option[Char],
    override val description: String,
    override val usage: Option[String] = None,
) extends BaseCommandLineOption {
    override def isRequired: Boolean = false
}
