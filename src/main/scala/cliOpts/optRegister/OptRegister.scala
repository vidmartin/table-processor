
package cliOpts.optRegister

abstract class OptRegister[T] extends BaseOptRegister {
    /** returns the parsed value or a default value; if neither is available, throw exception */
    def get: T = getOptional.get
    def getOptional: Option[T]
}

// TODO: range opt
// TODO: multi opt
