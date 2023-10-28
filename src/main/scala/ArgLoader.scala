
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
            throw new AlreadyExistsArgException(f"argument --${longName} was already added")
        }
        if (shortName.map(c => shortNameMap.contains(c)).getOrElse(false)) {
            throw new AlreadyExistsArgException(f"argument -${shortName} was already added")
        }

        longNameMap.addOne((longName, opt))
        shortName.foreach(c => shortNameMap.addOne((c, opt)))

        return opt
    }

    def load(args: Array[String]): Unit = {
        val it = args.iterator

        while (it.hasNext) {
            this.loadNext(it)
        }
    }

    private def loadNext(it: Iterator[String]): Unit = {
        val curr = it.next()

        val opt: BaseOpt = this.matchOpt(curr);

        if (opt.isDefined) {
            throw new MultipleOccurencesArgException(f"cannot redefine argument ${curr}")
        }
        try {
            opt.load(it)
        } catch {
            case e: NoSuchElementException => throw new IncompleteArgException(f"no value provided for argument ${curr}")
        }
    }

    private def matchOpt(optString: String): BaseOpt = {
        if (optString.startsWith("--") && optString.length() > 2) {
            val key = optString.substring(2)
            longNameMap.getOrElse(key, throw new UnknownArgException(f"unknown argument ${optString}"))
        } else if (optString.startsWith("-") && optString.length() > 1) {
            val key = optString(1)
            shortNameMap.getOrElse(key, throw new UnknownArgException(f"unknown argument ${optString}"))
        } else {
            throw new UnexpectedPositionArgException(f"unexpected positional argument '${optString}'")
        }
    }
}
