
package cliOpts

import scala.collection.immutable.HashMap
import cliOpt.BaseCommandLineOption

/** a collection of BaseCommandLineOptions indexable by their long names (e.g. --input-file) and possibly short names (e.g. -i) */
class OptRegistry extends Iterable[BaseCommandLineOption] {
    private var longNameMap: HashMap[String, BaseCommandLineOption] = new HashMap()
    private var shortNameMap: HashMap[Char, BaseCommandLineOption] = new HashMap()
    private var allNames: List[(String, Option[Char])] = List.empty

    def addOption[T <: BaseCommandLineOption](option: T): T = {
        if (longNameMap.contains(option.longName)) {
            throw new AlreadyExistsArgException(f"argument --${option.longName} was already added")
        }
        if (option.shortName.map(c => shortNameMap.contains(c)).getOrElse(false)) {
            throw new AlreadyExistsArgException(f"argument -${option.shortName} was already added")
        }

        longNameMap = longNameMap.updated(option.longName, option)
        option.shortName.foreach {
            c => shortNameMap = shortNameMap.updated(c, option)
        }
        allNames = allNames.appended((option.longName, option.shortName))

        option
    }

    override def iterator: Iterator[BaseCommandLineOption] = longNameMap.valuesIterator
    def getByLongName(longName: String): Option[BaseCommandLineOption] = longNameMap.get(longName)
    def getByShortName(shortName: Char): Option[BaseCommandLineOption] = shortNameMap.get(shortName)
    def getAll: List[(String, Option[Char], BaseCommandLineOption)] = {
        allNames.map {
            case ((longName, shortName)) => (longName, shortName, longNameMap.get(longName).get)
        }.toList
    }
}
