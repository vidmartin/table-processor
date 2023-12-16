
package cliOpts.optRegister

abstract class BaseOptRegister {
    /** parses the value from the iterator of arguments (does nothing if isDefined is true) */
    def load(it: Iterator[String]): Unit = {
        if (!this.isDefined) {
            this.forceLoad(it)
        }
    }
    /** parses the value from the iterator of arguments */
    protected def forceLoad(it: Iterator[String]): Unit
    /** whether a value was specified by the user or a default value is available */
    def hasValue: Boolean
    /** whether a value for this argument is fully defined */
    def isDefined: Boolean
    /** whether a call to load will advance the passed iterator */
    def hasArgs: Boolean
}