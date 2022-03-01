package miragefairy2019.mod3.fairystickcraft

import miragefairy2019.mod3.fairystickcraft.api.IFairyStickCraftCondition
import miragefairy2019.mod3.fairystickcraft.api.IFairyStickCraftEnvironment
import miragefairy2019.mod3.fairystickcraft.api.IFairyStickCraftExecutor
import miragefairy2019.mod3.fairystickcraft.api.IFairyStickCraftRecipe
import miragefairy2019.mod3.fairystickcraft.api.IFairyStickCraftRegistry
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.function.Consumer

class FairyStickCraftRegistry : IFairyStickCraftRegistry {
    override val recipes = mutableListOf<IFairyStickCraftRecipe>()
    override fun addRecipe(recipe: IFairyStickCraftRecipe) = run<Unit> { recipes.add(recipe) }

    override fun getExecutor(player: EntityPlayer?, world: World, blockPos: BlockPos, getterItemStackFairyStick: () -> ItemStack): IFairyStickCraftExecutor? {
        recipe@ for (recipe in recipes) {
            val environment: IFairyStickCraftEnvironment = FairyStickCraftEnvironment(player, world, blockPos, getterItemStackFairyStick)
            val executor: IFairyStickCraftExecutor = FairyStickCraftExecutor()
            for (condition in recipe.conditions) {
                if (!condition.test(environment, executor)) continue@recipe
            }
            return executor
        }
        return null
    }
}

class FairyStickCraftRecipe : IFairyStickCraftRecipe {
    override val conditions = mutableListOf<IFairyStickCraftCondition>()
}

class FairyStickCraftEnvironment(
    override val player: EntityPlayer?,
    override val world: World,
    override val blockPos: BlockPos,
    private val getterItemStackFairyStick: () -> ItemStack
) : IFairyStickCraftEnvironment {
    private val entitiesItemRemaining = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(blockPos).grow(1.0)).map { it!! }.toMutableList()
    override val itemStackFairyStick get() = getterItemStackFairyStick()
    override fun pullItem(ingredient: (ItemStack) -> Boolean): EntityItem? {
        val iterator = entitiesItemRemaining.iterator()
        while (iterator.hasNext()) {
            val entity = iterator.next()
            if (ingredient(entity.item)) {
                iterator.remove()
                return entity
            }
        }
        return null
    }
}

class FairyStickCraftExecutor : IFairyStickCraftExecutor {
    private val listenersOnUpdate = mutableListOf<() -> Unit>()
    override fun hookOnUpdate(listener: () -> Unit) = run<Unit> { listenersOnUpdate.add(listener) }
    override fun onUpdate() = listenersOnUpdate.forEach { it() }
    private val listenersOnCraft = mutableListOf<((ItemStack) -> Unit) -> Unit>()
    override fun hookOnCraft(listener: ((ItemStack) -> Unit) -> Unit) = run<Unit> { listenersOnCraft.add(listener) }
    override fun onCraft(setterItemStackFairyStick: (ItemStack) -> Unit) = listenersOnCraft.forEach { it(setterItemStackFairyStick) }
}
