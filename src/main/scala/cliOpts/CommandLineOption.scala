
package cliOpts
import optRegister.{BaseOptRegister, OptRegister}

abstract class BaseCommandLineOption(
) {
    def optRegister: BaseOptRegister
    def longName: String
    def shortName: Option[Char]
    def description: String
    def isRequired: Boolean
}

class CommandLineOption[T](
    override val optRegister: OptRegister[T],
    override val longName: String,
    override val shortName: Option[Char],
    override val description: String
) extends BaseCommandLineOption {
    override def isRequired: Boolean = false
}

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
