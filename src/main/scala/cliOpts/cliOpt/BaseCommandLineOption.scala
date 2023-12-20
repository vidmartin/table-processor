
package cliOpts.cliOpt

import cliOpts.optRegister._

/** specifies an option that the user can pass to the program via CLI arguments */
abstract class BaseCommandLineOption(
) {
    def optRegister: BaseOptRegister
    def longName: String
    def shortName: Option[Char]
    def description: String
    def usage: Option[String]
    def isRequired: Boolean
}
