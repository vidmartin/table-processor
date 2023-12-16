
package cliOpts.cliOpt

import cliOpts.optRegister._

abstract class BaseCommandLineOption(
) {
    def optRegister: BaseOptRegister
    def longName: String
    def shortName: Option[Char]
    def description: String
    def usage: Option[String]
    def isRequired: Boolean
}
