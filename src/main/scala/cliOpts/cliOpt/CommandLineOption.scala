
package cliOpts.cliOpt

import cliOpts.optRegister._

/** specifies an *optional* option that the user can pass to the program via CLI arguments */
class CommandLineOption[T](
    override val optRegister: OptRegister[T],
    override val longName: String,
    override val shortName: Option[Char],
    override val description: String,
    override val usage: Option[String] = None,
) extends BaseCommandLineOption {
    override def isRequired: Boolean = false
}
