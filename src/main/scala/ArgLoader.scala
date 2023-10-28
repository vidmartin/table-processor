
import scala.collection.mutable.HashMap

private class ArgLoader(registry: OptRegistry) {
    def load(args: Array[String]): Unit = {
        val it = args.iterator

        while (it.hasNext) {
            this.loadNext(it)
        }

        val missingOpts = getMissingOpts()
        if (missingOpts.nonEmpty) {
            throw new MissingArgException(
                f"the following required args were not specified: ${missingOpts.mkString(", ")}"
            )
        }
    }

    private def loadNext(it: Iterator[String]): Unit = {
        val curr = it.next()
        val opt: BaseOptRegister = this.matchOpt(curr);

        if (opt.isDefined) {
            throw new MultipleOccurencesArgException(f"cannot redefine argument ${curr}")
        }
        try {
            opt.load(it)
        } catch {
            case e: NoSuchElementException => throw new IncompleteArgException(f"no value provided for argument ${curr}")
        }
    }

    private def matchOpt(optString: String): BaseOptRegister = {
        if (optString.startsWith("--") && optString.length() > 2) {
            val key = optString.substring(2)
            registry.getByLongName(key).getOrElse(throw new UnknownArgException(f"unknown argument ${optString}")).optRegister
        } else if (optString.startsWith("-") && optString.length() > 1) {
            val key = optString(1)
            registry.getByShortName(key).getOrElse(throw new UnknownArgException(f"unknown argument ${optString}")).optRegister
        } else {
            throw new UnexpectedPositionArgException(f"unexpected positional argument '${optString}'")
        }
    }

    private def getMissingOpts(): Array[String] = {
        registry.iterator.filter(
            { case cliOpt => cliOpt.isRequired && !cliOpt.optRegister.hasValue }
        ).map(
            { case cliOpt => cliOpt.longName }
        ).toArray
    }
}

object ArgLoader {
    def load(registry: OptRegistry, args: Array[String]): Unit = {
        new ArgLoader(registry).load(args)
    }
}
