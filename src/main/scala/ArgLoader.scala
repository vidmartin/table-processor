
import scala.collection.mutable.HashMap

private class ArgLoader(registry: OptRegistry) {
    def load(args: Array[String], swallowMissingArgs: Option[() => Boolean] = None): Boolean = {
        val it = args.iterator

        while (it.hasNext) {
            this.loadNext(it)
        }

        val missingOpts = getMissingOpts()
        if (missingOpts.nonEmpty) {
            if (swallowMissingArgs.map(_()).getOrElse(false)) {
                return false
            }

            throw new MissingArgException(
                f"the following required args were not specified: ${missingOpts.mkString(", ")}"
            )
        }

        return true
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
            case e: FormatArgException => throw new FormatArgException(f"format error for ${curr} - ${e.getMessage()}")
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
    def load(registry: OptRegistry, args: Array[String], swallowMissingArgs: Option[() => Boolean] = None): Boolean = {
        new ArgLoader(registry).load(args, swallowMissingArgs)
    }
}