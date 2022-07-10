package miragefairy2019.lib.modinitializer

import miragefairy2019.common.ResourceName

abstract class ObjectScope<out T : Any>(private val getter: () -> T) : () -> T {
    internal val initializingObject get() = getter()
    override operator fun invoke() = initializingObject
}

interface NamedScope {
    val modScope: ModScope
    val resourceName: ResourceName
}
