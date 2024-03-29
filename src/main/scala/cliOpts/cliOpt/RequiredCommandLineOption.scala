
package cliOpts.cliOpt

import cliOpts.optRegister._

/** specifies a *required* option that the user can pass to the program via CLI arguments */
class RequiredCommandLineOption[T](
    override val optRegister: OptRegister[T],
    override val longName: String,
    override val shortName: Option[Char],
    override val description: String,
    override val usage: Option[String] = None,
) extends CommandLineOption[T](
    optRegister, longName, shortName, description, usage
) {
    override def isRequired: Boolean = true
}
