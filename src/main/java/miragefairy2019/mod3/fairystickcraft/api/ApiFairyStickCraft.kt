package miragefairy2019.mod3.fairystickcraft.api

import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object ApiFairyStickCraft {
    lateinit var fairyStickCraftRegistry: IFairyStickCraftRegistry
}

interface IFairyStickCraftRegistry {
    val recipes: Iterable<IFairyStickCraftRecipe>
    fun addRecipe(recipe: IFairyStickCraftRecipe)
    fun getExecutor(player: EntityPlayer?, world: World, blockPos: BlockPos, getterItemStackFairyStick: () -> ItemStack): IFairyStickCraftExecutor?
}

interface IFairyStickCraftRecipe {
    val conditions: Iterable<IFairyStickCraftCondition>
}

interface IFairyStickCraftCondition {
    fun test(environment: IFairyStickCraftEnvironment, executor: IFairyStickCraftExecutor): Boolean
    val ingredientsInput: Iterable<Iterable<ItemStack>> get() = emptyList()
    val stringsInput: Iterable<String> get() = emptyList()
    val ingredientsOutput: Iterable<Iterable<ItemStack>> get() = emptyList()
    val stringsOutput: Iterable<String> get() = emptyList()
}

interface IFairyStickCraftEnvironment {
    val player: EntityPlayer?
    val world: World
    val blockPos: BlockPos
    val itemStackFairyStick: ItemStack
    fun pullItem(ingredient: (ItemStack) -> Boolean): EntityItem?
}

interface IFairyStickCraftExecutor {
    fun onUpdate()
    fun hookOnUpdate(listener: () -> Unit)
    fun onCraft(setterItemStackFairyStick: (ItemStack) -> Unit)
    fun hookOnCraft(listener: ((ItemStack) -> Unit) -> Unit)
}
