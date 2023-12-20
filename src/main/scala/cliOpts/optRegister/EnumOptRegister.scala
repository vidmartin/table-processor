
package cliOpts.optRegister

import enum._
import cliOpts.FormatArgException

/** loads & stores an option of enum type T (load only advances the iterator once) */
class EnumOptRegister[T <: EnumCase](val companion: EnumCompanion[T]) extends ParseOptRegister[T] {
    override def withDefault(default: T): EnumOptRegister[T] = {
        this.default = Some(default)
        this
    }
    override def parse(s: String): T = companion.parse(s).getOrElse(
        throw new FormatArgException(f"invalid value ${s} - expected one of {${companion.all.map(c => c.name).mkString(", ")}}")
    )
    override def accept[T](visitor: OptRegisterVisitor[T]): T = {
        visitor.visitEnumOptRegister(this)
    }
}
