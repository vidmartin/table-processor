
import org.scalatest.FunSuite

class FlagsTestKit {
    val loader = new ArgLoader()
        
    val a = loader.addOption(new FlagOpt, "alpha", Some('a'))
    val b = loader.addOption(new FlagOpt, "beta", Some('b'))
    val c = loader.addOption(new FlagOpt, "gamma", Some('c'))
}

sealed abstract class GreekLetter;
object GreekLetter {
    object ALPHA extends GreekLetter
    object BETA extends GreekLetter
    object GAMMA extends GreekLetter
    object DELTA extends GreekLetter
    object EPSILON extends GreekLetter

    def parse(s: String) = s.toLowerCase() match {
        case "alpha" => ALPHA
        case "beta" => BETA
        case "gamma" => GAMMA
        case "delta" => DELTA
        case "epsilon" => EPSILON
    }
}

class MiscTestKit {
    val loader = new ArgLoader();

}

class ArgLoaderTest extends FunSuite {
    test("flags1") {
        val kit = new FlagsTestKit
        kit.loader.load(Array("--alpha", "-c"))
        
        assert(kit.a.get == true)
        assert(kit.b.get == false)
        assert(kit.c.get == true)
    }

    test("flags2") {
        val kit = new FlagsTestKit
        kit.loader.load(new Array(0))

        assert(kit.a.get == false)
        assert(kit.b.get == false)
        assert(kit.c.get == false)
    }

    test("flags3") {
        val kit = new FlagsTestKit
        kit.loader.load(Array("--beta"))

        assert(kit.a.get == false)
        assert(kit.b.get == true)
        assert(kit.c.get == false)
    }

    test("flags4") {
        val kit = new FlagsTestKit

        assertThrows[UnexpectedPositionArgException] {
            kit.loader.load(Array("--alpha", "hello"))
        }
    }

    test("flags5") {
        val kit = new FlagsTestKit

        assertThrows[UnknownArgException] {
            kit.loader.load(Array("--alpha", "--sigma"))
        }
    }

    test("flags6") {
        val kit = new FlagsTestKit

        assertThrows[MultipleOccurencesArgException] {
            kit.loader.load(Array("--alpha", "--alpha"))
        }
    }

    test("flags7") {
        val kit = new FlagsTestKit

        assertThrows[MultipleOccurencesArgException] {
            kit.loader.load(Array("--alpha", "-a"))
        }
    }
}
