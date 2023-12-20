
package cliOpts.optRegister

/** an OptRegister that doesn't actually store the loaded value, instead passes it to the function passed in via constructor params */
abstract class DelegatingOptRegister[T](load: T => Unit) extends OptRegister[T] {
    override protected def forceLoad(it: Iterator[String]): Unit = load(getValueToLoad(it))
    protected def getValueToLoad(it: Iterator[String]): T
    override def hasValue: Boolean = false
    override def isDefined: Boolean = false
}
