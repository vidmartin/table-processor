
package cliOpts.cliOpt

import cliOpts.optRegister._

class RequiredCommandLineOption[T](
    override val optRegister: OptRegister[T],
    override val longName: String,
    override val shortName: Option[Char],
    override val description: String
) extends CommandLineOption[T](
    optRegister, longName, shortName, description
) {
    override def isRequired: Boolean = true
}