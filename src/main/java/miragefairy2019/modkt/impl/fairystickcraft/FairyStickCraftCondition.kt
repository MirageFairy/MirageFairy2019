package miragefairy2019.modkt.impl.fairystickcraft

import miragefairy2019.modkt.api.fairystickcraft.IFairyStickCraftCondition
import miragefairy2019.modkt.api.fairystickcraft.IFairyStickCraftEnvironment
import miragefairy2019.modkt.api.fairystickcraft.IFairyStickCraftExecutor
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraftforge.common.BiomeDictionary

class FairyStickCraftConditionSpawnItem(private val sItemStack: () -> ItemStack) : IFairyStickCraftCondition {
    override fun test(environment: IFairyStickCraftEnvironment, executor: IFairyStickCraftExecutor): Boolean {
        executor.hookOnCraft {
            if (!environment.world.isRemote) {
                val entityitem = EntityItem(
                    environment.world,
                    environment.blockPos.x + 0.5,
                    environment.blockPos.y + 0.5,
                    environment.blockPos.z + 0.5,
                    sItemStack().copy()
                )
                entityitem.setNoPickupDelay()
                environment.world.spawnEntity(entityitem)
            }
        }
        return true
    }

    override val ingredientsOutput get() = listOf(listOf(sItemStack()))
}

class FairyStickCraftConditionConsumeItem @JvmOverloads constructor( // TODO remove jvm annotation
    private val ingredient: Ingredient,
    private val count: Int = 1
) : IFairyStickCraftCondition {
    override fun test(environment: IFairyStickCraftEnvironment, executor: IFairyStickCraftExecutor): Boolean {
        // TODO 同種のアイテムを登録したとき、スタックされていると反応しない

        // アイテムを抽出する
        val entity = environment.pullItem { ingredient.apply(it) && it.count >= count } ?: return false

        executor.hookOnCraft {
            entity.item.shrink(count)
            if (entity.item.isEmpty) environment.world.removeEntity(entity)
            environment.world.spawnParticle(EnumParticleTypes.SPELL_MOB, entity.posX, entity.posY, entity.posZ, 1.0, 0.0, 0.0)
        }
        executor.hookOnUpdate {
            repeat(2) {
                environment.world.spawnParticle(EnumParticleTypes.SPELL_MOB, entity.posX, entity.posY, entity.posZ, 0.0, 1.0, 0.0)
            }
        }
        return true
    }

    override val ingredientsInput
        get() = listOf(ingredient.matchingStacks.map {
            val it2 = it.copy()
            it2.count = count
            it2
        })
}

class FairyStickCraftConditionSpawnBlock(private val sBlockStateInput: () -> IBlockState) : IFairyStickCraftCondition {
    override fun test(environment: IFairyStickCraftEnvironment, executor: IFairyStickCraftExecutor): Boolean {
        val world = environment.world
        val blockPos = environment.blockPos
        val blockState = sBlockStateInput()

        // 設置先は空気でなければならない
        if (!environment.world.getBlockState(environment.blockPos).block.isReplaceable(world, blockPos)) return false

        // 設置物は召喚先に設置可能でなければならない
        if (!blockState.block.canPlaceBlockAt(world, blockPos)) return false

        executor.hookOnCraft {
            world.setBlockState(blockPos, blockState, 3)
            world.neighborChanged(blockPos, blockState.block, blockPos.up())
            world.playSound(null, blockPos, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.2f, 1.0f)
            repeat(20) {
                world.spawnParticle(
                    EnumParticleTypes.VILLAGER_HAPPY,
                    blockPos.x + world.rand.nextDouble(),
                    blockPos.y + world.rand.nextDouble(),
                    blockPos.z + world.rand.nextDouble(),
                    0.0, 0.0, 0.0
                )
            }
        }
        executor.hookOnUpdate {
            repeat(3) {
                world.spawnParticle(
                    EnumParticleTypes.END_ROD,
                    blockPos.x + world.rand.nextDouble(),
                    blockPos.y + world.rand.nextDouble(),
                    blockPos.z + world.rand.nextDouble(),
                    0.0, 0.0, 0.0
                )
            }
        }
        return true
    }

    override val stringsOutput get() = listOf(sBlockStateInput().block.localizedName)

}

class FairyStickCraftConditionConsumeBlock(private val sBlockStateInput: () -> IBlockState) : IFairyStickCraftCondition {
    override fun test(environment: IFairyStickCraftEnvironment, executor: IFairyStickCraftExecutor): Boolean {
        val world = environment.world
        val blockPos = environment.blockPos
        val blockState = Blocks.AIR.defaultState

        // 設置先は指定されたブロックでなければならない
        if (environment.world.getBlockState(environment.blockPos) != sBlockStateInput()) return false

        executor.hookOnCraft {
            world.setBlockState(blockPos, blockState, 3)
            world.neighborChanged(blockPos, blockState.block, blockPos.up())
            world.playSound(null, blockPos, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.2f, 1.0f)
            repeat(20) {
                world.spawnParticle(
                    EnumParticleTypes.VILLAGER_HAPPY,
                    blockPos.x + world.rand.nextDouble(),
                    blockPos.y + world.rand.nextDouble(),
                    blockPos.z + world.rand.nextDouble(),
                    0.0, 0.0, 0.0
                )
            }
        }
        executor.hookOnUpdate {
            repeat(3) {
                world.spawnParticle(
                    EnumParticleTypes.END_ROD,
                    blockPos.x + world.rand.nextDouble(),
                    blockPos.y + world.rand.nextDouble(),
                    blockPos.z + world.rand.nextDouble(),
                    0.0, 0.0, 0.0
                )
            }
        }
        return true
    }

    override val stringsInput get() = listOf(sBlockStateInput().block.localizedName)
}

class FairyStickCraftConditionReplaceBlock(private val sBlockStateInput: () -> IBlockState, private val sBlockStateOutput: () -> IBlockState) : IFairyStickCraftCondition {
    override fun test(environment: IFairyStickCraftEnvironment, executor: IFairyStickCraftExecutor): Boolean {
        val world = environment.world
        val blockPos = environment.blockPos
        val blockState = sBlockStateOutput()

        // 設置先は指定されたブロックでなければならない
        if (environment.world.getBlockState(environment.blockPos) != sBlockStateInput()) return false

        executor.hookOnCraft {
            world.setBlockState(blockPos, blockState, 3)
            world.neighborChanged(blockPos, blockState.block, blockPos.up())
            world.playSound(null, blockPos, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.2f, 1.0f)
            repeat(20) {
                world.spawnParticle(
                    EnumParticleTypes.VILLAGER_HAPPY,
                    blockPos.x + world.rand.nextDouble(),
                    blockPos.y + world.rand.nextDouble(),
                    blockPos.z + world.rand.nextDouble(),
                    0.0, 0.0, 0.0
                )
            }
        }
        executor.hookOnUpdate {
            repeat(3) {
                world.spawnParticle(
                    EnumParticleTypes.END_ROD,
                    blockPos.x + world.rand.nextDouble(),
                    blockPos.y + world.rand.nextDouble(),
                    blockPos.z + world.rand.nextDouble(),
                    0.0, 0.0, 0.0
                )
            }
        }
        return true
    }

    override val stringsInput get() = listOf(sBlockStateInput().block.localizedName)
    override val stringsOutput get() = listOf(sBlockStateOutput().block.localizedName)
}

class FairyStickCraftConditionUseItem(private val ingredient: Ingredient) : IFairyStickCraftCondition {
    override fun test(environment: IFairyStickCraftEnvironment, executor: IFairyStickCraftExecutor): Boolean {

        if (!ingredient.apply(environment.itemStackFairyStick)) return false

        executor.hookOnCraft { setterItemStackFairyStick ->
            val itemStackFairyStick = environment.itemStackFairyStick

            // コンテナアイテム計算
            val itemStackContainer = if (itemStackFairyStick.item.hasContainerItem(itemStackFairyStick)) {
                itemStackFairyStick.item.getContainerItem(itemStackFairyStick)
            } else {
                ItemStack.EMPTY
            }

            // 破損
            setterItemStackFairyStick(itemStackContainer)

            // エフェクト
            val player = environment.player
            if (player != null) {
                if (!itemStackFairyStick.isEmpty) {
                    if (itemStackContainer.isEmpty) {
                        player.renderBrokenItemStack(itemStackFairyStick)
                    }
                }
            }
        }
        return true
    }

    override val ingredientsInput get() = listOf(ingredient.matchingStacks.toList())
}

class FairyStickCraftConditionNotNether : IFairyStickCraftCondition {
    override fun test(environment: IFairyStickCraftEnvironment, executor: IFairyStickCraftExecutor): Boolean {
        return !BiomeDictionary.hasType(environment.world.getBiome(environment.blockPos), BiomeDictionary.Type.NETHER)
    }

    override val stringsInput get() = listOf("Not Nether")
}
