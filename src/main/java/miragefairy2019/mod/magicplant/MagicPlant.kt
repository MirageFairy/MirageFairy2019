package miragefairy2019.mod.magicplant

import miragefairy2019.api.IPickExecutor
import miragefairy2019.api.IPickHandler
import miragefairy2019.api.PickHandlerRegistry
import miragefairy2019.lib.modinitializer.module

val magicPlantModule = module {
    mirageFlowerModule()
    mandrakeModule()

    onRegisterBlock {
        PickHandlerRegistry.pickHandlers += IPickHandler { world, blockPos, player ->
            val blockState = world.getBlockState(blockPos)
            val block = blockState.block as? BlockMagicPlant ?: return@IPickHandler null
            if (!block.isMaxAge(blockState)) return@IPickHandler null
            IPickExecutor { fortune -> block.tryPick(world, blockPos, player, fortune) }
        }
    }

}
