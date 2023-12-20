
package cliOpts.optRegister

/** a stateful object for loading a value of type T from an iterator and storing it */
abstract class OptRegister[T] extends BaseOptRegister {
    /** returns the parsed value or a default value; if neither is available, throw exception */
    def get: T = getOptional.get
    /** returns the parsed value or a default value; if neither is available, return None */
    def getOptional: Option[T]
}
