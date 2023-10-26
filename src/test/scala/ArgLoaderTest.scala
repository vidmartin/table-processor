
import org.scalatest.FunSuite

class FlagsTestKit {
    val loader = new ArgLoader()
        
    val a = loader.addOption(new FlagOpt, "alpha", Some('a'))
    val b = loader.addOption(new FlagOpt, "beta", Some('b'))
    val c = loader.addOption(new FlagOpt, "gamma", Some('c'))
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
}
