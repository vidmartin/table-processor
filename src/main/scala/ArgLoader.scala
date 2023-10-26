
import scala.collection.mutable.HashMap

class ArgLoader {
    private val longNameMap: HashMap[String, BaseOpt] = new HashMap()
    private val shortNameMap: HashMap[Char, BaseOpt] = new HashMap()

    def addOption[T <: BaseOpt](
        opt: T,
        longName: String,
        shortName: Option[Char] = None,
    ): T = {
        if (longNameMap.contains(longName)) {
            throw new Exception() // TODO: more specific exception
        }
        if (shortName.map(c => shortNameMap.contains(c)).getOrElse(false)) {
            throw new Exception() // TODO: more specific exception
        }

        longNameMap.addOne((longName, opt))
        shortName.foreach(c => shortNameMap.addOne((c, opt)))

        return opt
    }

    def load(args: Array[String]): Unit = {
        val it = args.iterator
        while (it.hasNext) {
            val curr = it.next()
            val opt: BaseOpt = if (curr.startsWith("--") && curr.length() > 2) {
                longNameMap(curr.substring(2))
            } else if (curr.startsWith("-") && curr.length() > 1) {
                shortNameMap(curr(1))
            } else {
                throw new Exception() // TODO: more specific exception
            }
            opt.load(it)
        }
    }
}
