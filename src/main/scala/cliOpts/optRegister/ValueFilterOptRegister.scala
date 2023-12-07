
package cliOpts.optRegister

import filters.ValueFilter

class ValueFilterOptRegister(load: ValueFilter => Unit) extends DelegatingOptRegister[ValueFilter](load) {
    override def hasValue: Boolean = ???
    override def isDefined: Boolean = ???
    override def getOptional: Option[ValueFilter] = ???
    override protected def getValueToLoad(it: Iterator[String]): ValueFilter = ???
}
