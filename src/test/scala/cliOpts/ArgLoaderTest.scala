
package cliOpts

import enum._
import org.scalatest.FunSuite

class FlagsTestKit {
    val opts = new OptRegistry

    val a = opts.addOption(new CommandLineOption(
        new FlagOptRegister, "alpha", Some('a'), ""
    ))
    val b = opts.addOption(new CommandLineOption(
        new FlagOptRegister, "beta", Some('b'), ""
    ))
    val c = opts.addOption(new CommandLineOption(
        new FlagOptRegister, "gamma", Some('c'), ""
    ))
}

sealed case class GreekLetter(override val name: String) extends EnumCase
object GreekLetter extends EnumCompanion[GreekLetter] {
    object ALPHA extends GreekLetter("alpha")
    object BETA extends GreekLetter("beta")
    object GAMMA extends GreekLetter("gamma")
    object DELTA extends GreekLetter("delta")
    object EPSILON extends GreekLetter("epsilon")

    def all: Array[GreekLetter] = Array(ALPHA, BETA, GAMMA, DELTA, EPSILON)
}

class GreekTestKit {
    val opts = new OptRegistry

    val philosopherName = opts.addOption(new RequiredCommandLineOption(
        new StringOptRegister, "philosopher-name", Some('p'), ""
    ))
    val greekLetter = opts.addOption(new CommandLineOption(
        new EnumOptRegister(GreekLetter), "greek-letter", Some('g'), ""
    ))
    val nationality = opts.addOption(new CommandLineOption(
        new StringOptRegister().withDefault("Greek"), "nationality", None, ""
    ))
}

class ArgLoaderTest extends FunSuite {
    test("flags1") {
        val kit = new FlagsTestKit
        ArgLoader.load(kit.opts, Array("--alpha", "-c"))
        
        assert(kit.a.optRegister.get == true)
        assert(kit.b.optRegister.get == false)
        assert(kit.c.optRegister.get == true)
    }

    test("flags2") {
        val kit = new FlagsTestKit
        ArgLoader.load(kit.opts, new Array(0))

        assert(kit.a.optRegister.get == false)
        assert(kit.b.optRegister.get == false)
        assert(kit.c.optRegister.get == false)
    }

    test("flags3") {
        val kit = new FlagsTestKit
        ArgLoader.load(kit.opts, Array("--beta"))

        assert(kit.a.optRegister.get == false)
        assert(kit.b.optRegister.get == true)
        assert(kit.c.optRegister.get == false)
    }

    test("flags4") {
        val kit = new FlagsTestKit

        assertThrows[UnexpectedPositionArgException] {
            ArgLoader.load(kit.opts, Array("--alpha", "hello"))
        }
    }

    test("flags5") {
        val kit = new FlagsTestKit

        assertThrows[UnknownArgException] {
            ArgLoader.load(kit.opts, Array("--alpha", "--sigma"))
        }
    }

    test("flags6") {
        val kit = new FlagsTestKit

        assertThrows[MultipleOccurencesArgException] {
            ArgLoader.load(kit.opts, Array("--alpha", "--alpha"))
        }
    }

    test("flags7") {
        val kit = new FlagsTestKit

        assertThrows[MultipleOccurencesArgException] {
            ArgLoader.load(kit.opts, Array("--alpha", "-a"))
        }
    }

    test("greek1") {
        val kit = new GreekTestKit

        ArgLoader.load(kit.opts, Array("-p", "Aristotle", "-g", "alpha"))

        assert(kit.philosopherName.optRegister.get == "Aristotle")
        assert(kit.greekLetter.optRegister.get == GreekLetter.ALPHA)
        assert(kit.nationality.optRegister.get == "Greek")
    }

    test("greek2") {
        val kit = new GreekTestKit

        ArgLoader.load(kit.opts, Array("--philosopher-name", "Plato"))

        assert(kit.philosopherName.optRegister.get == "Plato")
        assertThrows[NoSuchElementException] { kit.greekLetter.optRegister.get }
        assert(kit.greekLetter.optRegister.hasValue == false)
        assert(kit.nationality.optRegister.get == "Greek")
    }

    test("greek3") {
        val kit = new GreekTestKit

        ArgLoader.load(kit.opts, Array("--greek-letter", "EPsiLon", "--philosopher-name", "Epictetus"))

        assert(kit.greekLetter.optRegister.get == GreekLetter.EPSILON)
        assert(kit.philosopherName.optRegister.get == "Epictetus")
        assert(kit.nationality.optRegister.get == "Greek")
    }

    test("greek4") {
        val kit = new GreekTestKit

        ArgLoader.load(kit.opts, Array("-p", "MarcusAurelius", "--nationality", "Roman"))

        assert(kit.greekLetter.optRegister.hasValue == false)
        assert(kit.philosopherName.optRegister.get == "MarcusAurelius")
        assert(kit.nationality.optRegister.get == "Roman")
    }

    test("greek5") {
        val kit = new GreekTestKit

        assertThrows[IncompleteArgException] {
            ArgLoader.load(kit.opts, Array("-g", "beta", "-p"))
        }
    }

    test("greek6") {
        val kit = new GreekTestKit

        assertThrows[MultipleOccurencesArgException] {
            ArgLoader.load(kit.opts, Array("-p", "Pythagoras", "--philosopher-name", "Thales"))
        }
    }

    test("greek7") {
        val kit = new GreekTestKit

        assertThrows[MissingArgException] {
            ArgLoader.load(kit.opts, Array("--greek-letter", "delta"))
        }
    }

    test("greek8") {
        val kit = new GreekTestKit

        assertThrows[FormatArgException] {
            ArgLoader.load(kit.opts, Array("-p", "Socrates", "--greek-letter", "sigma"))
        }
    }
}
