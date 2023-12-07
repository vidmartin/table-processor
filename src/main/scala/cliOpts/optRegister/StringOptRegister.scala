
package cliOpts.optRegister

class StringOptRegister extends ParseOptRegister[String] {
    override def withDefault(default: String): StringOptRegister = {
        this.default = Some(default)
        this
    }

    override def parse(s: String): String = s
}
