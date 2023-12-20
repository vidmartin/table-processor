
package enum

import scala.collection.immutable.HashMap

/** an interface for enum types - also remember to implement EnumCompanion! */
abstract class EnumCase {
    def name: String
}

/** interface providing utility functions for working with a given enum type */
abstract class EnumCompanion[T <: EnumCase] {
    def all: Array[T]

    lazy val map: HashMap[String, T] = HashMap.from(
        all.map(ec => (ec.name.toLowerCase(), ec))
    )

    def parse(s: String): Option[T] = map.get(s.toLowerCase())
}