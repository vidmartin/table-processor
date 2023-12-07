
package enum

import scala.collection.immutable.HashMap

abstract class EnumCase {
    def name: String
}

abstract class EnumCompanion[T <: EnumCase] {
    def all: Array[T]

    lazy val map: HashMap[String, T] = HashMap.from(
        all.map(ec => (ec.name.toLowerCase(), ec))
    )

    def parse(s: String): Option[T] = map.get(s.toLowerCase())
}