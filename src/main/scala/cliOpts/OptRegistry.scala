
package cliOpts

import scala.collection.mutable.{ListBuffer, HashMap}
import scala.collection.immutable.AbstractSeq

class OptRegistry extends Iterable[BaseCommandLineOption] {
    private val longNameMap: HashMap[String, BaseCommandLineOption] = new HashMap()
    private val shortNameMap: HashMap[Char, BaseCommandLineOption] = new HashMap()

    def addOption[T <: BaseCommandLineOption](option: T): T = {
        if (longNameMap.contains(option.longName)) {
            throw new AlreadyExistsArgException(f"argument --${option.longName} was already added")
        }
        if (option.shortName.map(c => shortNameMap.contains(c)).getOrElse(false)) {
            throw new AlreadyExistsArgException(f"argument -${option.shortName} was already added")
        }

        this.longNameMap.addOne((option.longName, option))
        option.shortName.foreach(c => shortNameMap.addOne((c, option)))
        option
    }

    override def iterator: Iterator[BaseCommandLineOption] = longNameMap.valuesIterator
    def getByLongName(longName: String): Option[BaseCommandLineOption] = longNameMap.get(longName)
    def getByShortName(shortName: Char): Option[BaseCommandLineOption] = shortNameMap.get(shortName)
}
