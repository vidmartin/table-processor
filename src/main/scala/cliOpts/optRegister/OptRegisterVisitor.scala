
package cliOpts.optRegister

import enum.EnumCase

/** visitor interface for the BaseOptRegister hiearchy */
abstract class OptRegisterVisitor[TReturn] {
    def visitColumnPredicateFilterOptRegister(reg: ColumnPredicateFilterOptRegister): TReturn
    def visitEnumOptRegister[T <: EnumCase](reg: EnumOptRegister[T]): TReturn
    def visitRangeOptRegister(reg: RangeOptRegister): TReturn
    def visitValueFilterOptRegister(reg: ValueFilterOptRegister): TReturn
    def visitStringOptRegister(reg: StringOptRegister): TReturn
    def visitFlagOptRegister(reg: FlagOptRegister): TReturn
}
