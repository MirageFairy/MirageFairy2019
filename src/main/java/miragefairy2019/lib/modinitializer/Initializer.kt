package miragefairy2019.lib.modinitializer

import miragefairy2019.common.ResourceName

// TODO rename: InitializingScope
abstract class Initializer<out T : Any>(private val getter: () -> T) : () -> T {
    internal val initializingObject get() = getter()
    override operator fun invoke() = initializingObject
}

interface NamedInitializer {
    val modInitializer: ModInitializer
    val resourceName: ResourceName
}
