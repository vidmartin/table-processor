
package cliOpts.optRegister

import enum._
import cliOpts.FormatArgException

class EnumOptRegister[T <: EnumCase](companion: EnumCompanion[T]) extends ParseOptRegister[T] {
    override def withDefault(default: T): EnumOptRegister[T] = {
        this.default = Some(default)
        this
    }
    override def parse(s: String): T = companion.parse(s).getOrElse(
        throw new FormatArgException(f"invalid value ${s} - expected one of {${companion.all.map(c => c.name).mkString(", ")}}")
    )
}
