
package cliOpts

import scala.collection.mutable.HashMap
import cliOpts.optRegister._
import cliOpts._

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
        val opts = this.matchOpts(curr)

        validateMatchedOpts(opts)

        for ((name, opt) <- opts) {
            try {
                opt.load(it)
            } catch {
                case e: NoSuchElementException => throw new IncompleteArgException(f"no value provided for argument ${name}")
                case e: FormatArgException => throw new FormatArgException(f"format error for ${name} - ${e.getMessage()}")
            }
        }
    }

    private def matchOpts(optString: String): List[(String, BaseOptRegister)] = {
        if (optString.startsWith("--") && optString.length() > 2) {
            val key = optString.substring(2)
            List((
                optString, 
                registry.getByLongName(key).getOrElse(
                    throw new UnknownArgException(f"unknown argument ${optString}")
                ).optRegister
            ))
        } else if (optString.startsWith("-") && optString.length() > 1) {
            optString.substring(1).map(
                key => (
                    f"-${key}",
                    registry.getByShortName(key).getOrElse(
                        throw new UnknownArgException(f"unknown argument -${key}")
                    ).optRegister
                )
            ).toList
        } else {
            throw new UnexpectedPositionArgException(f"unexpected positional argument '${optString}'")
        }
    }

    private def validateMatchedOpts(opts: Iterable[(String, BaseOptRegister)]) {
        var hadArgs: Boolean = false
        for ((name, opt) <- opts) {
            if (hadArgs) {
                throw new IncompleteArgException(f"option ${name} requires an argument, but was used as a flag")
            }
            if (opt.isDefined) {
                throw new MultipleOccurencesArgException(f"cannot redefine argument ${name}")
            }
            hadArgs = opt.hasArgs
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