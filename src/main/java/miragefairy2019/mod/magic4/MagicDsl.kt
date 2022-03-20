package miragefairy2019.mod.magic4

import net.minecraft.world.World

fun Magic?.getMagicHandler(magicArguments: MagicArguments) = this?.invoke(magicArguments) ?: MagicHandler()

fun magic(magic: Magic) = magic

val MagicArguments.world: World get() = player.world
