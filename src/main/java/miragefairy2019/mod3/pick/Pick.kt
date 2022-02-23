package miragefairy2019.mod3.pick

import miragefairy2019.libkt.module
import miragefairy2019.mod3.pick.api.IPickHandler
import miragefairy2019.mod3.pick.api.IPickHandlerRegistry
import miragefairy2019.mod3.worldgen.api.ApiWorldGen
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object Pick {
    val module = module {
        onInstantiation {
            ApiWorldGen.pickHandlerRegistry = PickHandlerRegistry()
        }
    }
}

abstract class PickHandler : IPickHandler {
    final override fun tryPick(world: World, blockPos: BlockPos, player: EntityPlayer?, fortune: Int): Boolean {
        return if (canPick(world.getBlockState(blockPos))) tryPickImpl(world, blockPos, player, fortune) else false
    }

    /** [canPick]が真の場合、右クリック時に呼び出されます。 */
    abstract fun tryPickImpl(world: World, blockPos: BlockPos, player: EntityPlayer?, fortune: Int): Boolean
}

class PickHandlerRegistry : IPickHandlerRegistry {
    val map = mutableMapOf<Block, IPickHandler>()
    override fun register(block: Block, pickHandler: IPickHandler) = run { map.put(block, pickHandler); Unit }
    override fun get(block: Block) = map[block]
}
