
package cliOpts.helpPrinter

import cliOpts.optRegister._
import enum.EnumCase
import filters.Comparator

/** used to get default usage annotations for different types of OptRegisters when printing help */
object UsageVisitor extends OptRegisterVisitor[Option[String]] {
    def visitColumnPredicateFilterOptRegister(reg: ColumnPredicateFilterOptRegister): Option[String] = {
        Some("[COLUMN]")
    }
    def visitEnumOptRegister[T <: EnumCase](reg: EnumOptRegister[T]): Option[String] = {
        Some(f"[${reg.companion.all.map(c => c.name).mkString("|")}]")
    }
    def visitRangeOptRegister(reg: RangeOptRegister): Option[String] = {
        Some("[UPPER LEFT] [LOWER RIGHT]")
    }
    def visitValueFilterOptRegister(reg: ValueFilterOptRegister): Option[String] = {
        Some(f"[COLUMN] [${Comparator.all.map(c => c.name).mkString("|")}] [VALUE]")
    }
    def visitStringOptRegister(reg: StringOptRegister): Option[String] = {
        Some("[STRING]")
    }
    def visitFlagOptRegister(reg: FlagOptRegister): Option[String] = None
}
